package lectures.part2fp

object Monads extends App {

  // our own monad
  trait Attempt[+A]{
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A]{
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing]{
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)


  // lazy monad

  class Lazy[+A](value: => A){
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: A=> Lazy[B]): Lazy[B] = f(value)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

}
