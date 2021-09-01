package lectures.part3conc

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._
object FuturesAndPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife
  } // (global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"$meaningOfLife")
    case Failure(e) => println(s"$e")
  } // SOME thread

  Thread.sleep(3000)


  // mini social network

  case class Profile(id: String, name: String) {
    def poke(another: Profile): Unit = println(s"${this.name} poking ${another.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-awe" -> "Awe",
      "fb.id.2-wel" -> "Wel",
      "fb.id.3-pop" -> "Pop"
    )

    val friends = Map(
      "fb.id.1-awe" -> "fb.id.2-wel"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

  }

  // client: awe to pake wel
  val awe = SocialNetwork.fetchProfile("fb.id/1-awe")
  awe.onComplete{
    case Success(aweProfile) =>
      val wel = SocialNetwork.fetchBestFriend(aweProfile)
      wel.onComplete{
        case Success(welProfile) => aweProfile.poke(welProfile)
        case Failure(e) => e.printStackTrace()
      }

    case Failure(ex) => ex.printStackTrace()
  }
  Thread.sleep(1000)


  // functional composition of futures
  // map, flatMap, filter
  val nameOnTheWall = awe.map(profile => profile.name)

  val aweBestFriend = awe.flatMap(profile => SocialNetwork.fetchBestFriend(profile))


  val someBestFriendRestricted = aweBestFriend.filter(profile => profile.name.startsWith("Z"))


  // for comprehension

  for {
    awe <- SocialNetwork.fetchProfile("fb.id.1-awe")
    wel <- SocialNetwork.fetchBestFriend(awe)
  } awe.poke(wel)

  // works as well

  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case _: Throwable => Profile("fb.id.0-dummy", "Something")
  }

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case _: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }

  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))


  // online banking app

  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Scala Banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }
    def calculateTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some process
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(userName: String, item: String, merchantName: String, cost: Double): String = {
      val transactionStatusFuture = for {
        user <- fetchUser(userName)
        transaction <- calculateTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds) // implicit converstions
    }
  }

  // promises


  val aPromise = Promise[Int]() // "controller" over a future
  val future = aPromise.future

  // Prod Cons with promises

  // thread 1 - "consumer"
  future.onComplete{
    case Success(r) => println("[consumer] received " + r)
  }
  val producer = new Thread(() => {
    println("[producer]")
    Thread.sleep(1000)
    aPromise.success(42)
    println("[producer] done")
  })
  // producer.start()


  // 1 - Future IMMEDIATELY with a value
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2 - inSequence(fa, fb)
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second)

  // first(fa, fb) => new future with the first value of the two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]

    def tryComplete(promise: Promise[A], result: Try[A]): Unit = result match {
      case Success(r) => try{
        promise.success(r)
      } catch {
        case _ =>
      }
      case Failure(t) => try {
        promise.failure(t)
      } catch {
        case _ =>
      }
    }

    fa.onComplete(tryComplete(promise, _)) // promise.tryComplete INSTEAD COULD BE APPLIED
    fb.onComplete(tryComplete(promise, _))

    promise.future
  }

  // last(fa, fb) => new future with the last value

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2 promise which the LAST future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]

    val checkAndComplete = (result: Try[A]) => if(!bothPromise.tryComplete(result))
      lastPromise.complete(result)
    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }

  // retry until
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }
}
