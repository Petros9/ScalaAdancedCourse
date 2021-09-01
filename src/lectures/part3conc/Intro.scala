package lectures.part3conc

import java.util.concurrent.Executors

object Intro extends App {


  /*
  interface Runnable{
    public void run()
  }
   */
  // JVM threads

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start() // gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread
  // start works only on thread

  aThread.join() // blocks until aThread finishes running
  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))


  // threadHello.start()
  // threadGoodbye.start()



  /*
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("sssss"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("1")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("1 1")
    Thread.sleep(2000)
    println("1 2")
  })
 */
 // pool.shutdown()
 // pool.execute(() => println("aaa")) // exception

  // pool.shutdownNow()

  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread2.start()
    thread1.start()
    println(x)
  }

  // for(_ <- 1 to 1000) runInParallel

  // race condition

  class BankAccount(var amount: Int){
    override def toString: String = "" + amount
  }

  def buy(@volatile account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //println(s"I've bought a $thing")

    //println(s"my account is now $account")
  }

  for(_ <- 1 to 1000){
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
    val thread2 = new Thread(() => buySafe(account, "X", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(10)
    if(account.amount != 43000) println("AHA: " + account.amount)
  }

  // solution 1 - use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized(
      // no two thread can evaluate this at the same time
      account.amount -= price
    )
  }

  // solution 2 - use @volatile // not as safe

def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = {
  new Thread(() => {
    if(i < maxThreads){
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })
}


  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())


  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "scala is ok"
  })

  message = "Scala is bad"
  awesomeThread.start()
  Thread.sleep(2000)
  println(message)
  // pretty much anytime it should be "scala is ok", but it is not guaranteed

}
