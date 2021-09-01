package lectures.part3conc

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  // PRODUCER CONSUMER PROBLEM


  class SimpleContainer {
    private var value: Int = 0
    def isEmpty: Boolean = value == 0
    def set(newValue: Int): Unit = value = newValue
    def get: Int = {
      val result = value
      value = 0
      result
    }

    def naiveProdCons(): Unit = {
      val container = new SimpleContainer

      val consumer = new Thread(() => {
        printf("[consumer] waiting ...")

        while(container.isEmpty) {
          println("[consumer] waiting")
        }

        println("[consumer] consuming" + container.get)
      })

      val producer = new Thread(() => {
        println("[producer] computing")
        Thread.sleep(500)
        val value = 42
        println("[producer] producing")
        container.set(value)
      })

      consumer.start()
      producer.start()
    }
   // naiveProdCons()



    def betterProdCons(): Unit = {
      val container = new SimpleContainer

      val consumer = new Thread(() => {
        println("[consumer] waiting")

        container.synchronized(
          container.wait()
        )
        println("[consumer] consuming" + container.get)
      })

      val producer = new Thread(() => {
        println("[producer] starting")
        Thread.sleep(500)
        val value = 42

        container.synchronized {
          println("[producer] producing")
          container.set(value)
          container.notify()
        }
      })

      consumer.start()
      producer.start()
    }
    // betterProdCons()


    def prodConsLargeBuffer(): Unit = {
      val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
      val capacity = 3

      val consumer = new Thread(() => {
        val random = new Random()
        while(true){
          buffer.synchronized{
            if(buffer.isEmpty){
              println("[consumer] buffer empty waiting")
              buffer.wait()
            }

            // there must be at least one value
            val x = buffer.dequeue()
            println("[consumer] I got " + x)
            buffer.notify()
          }
          Thread.sleep(random.nextInt(500))
        }
      })

      val producer = new Thread(() => {
        val random = new Random()
        val i = 0

        while(true){
          buffer.synchronized {
            if (buffer.size == capacity) {
              println("[producer] buffer is waiting")
              buffer.wait()
            }

            // there must be at least ONE EMPTY SPACE in the buffer
            println("[producer] producing " + i)
            buffer.enqueue(i)

            buffer.notify()
          }
          Thread.sleep(random.nextInt(500))
        }
      })
      producer.start()
      consumer.start()
    }

    // prodConsLargeBuffer()


    // MULTIPLE CONSUMERS AND PRODUCERS

    class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
      override def run(): Unit = {
        val random = new Random()

        while(true){
          buffer.synchronized{
            while(buffer.isEmpty){
              println(s"[consumer $id] buffer empty waiting")
              buffer.wait()
            }

            // there must be at least one value
            val x = buffer.dequeue()
            println(s"[consumer $id] I got " + x)
            buffer.notify()
          }
          Thread.sleep(random.nextInt(500))
        }
      }
    }

    class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
      override def run(): Unit = {
        val random = new Random()
        val i = 0

        while(true){
          buffer.synchronized {
            while (buffer.size == capacity) {
              println(s"[producer $id] buffer is waiting")
              buffer.wait()
            }

            // there must be at least ONE EMPTY SPACE in the buffer
            println(s"[producer $id] producing " + i)
            buffer.enqueue(i)

            buffer.notify()
          }
          Thread.sleep(random.nextInt(500))
        }
      }
    }

    def multiProdCons(consumers: Int, producers: Int): Unit = {
      val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
      val capacity = 3

      (1 to consumers).foreach(i => new Consumer(i, buffer).start())
      (1 to producers).foreach(i => new Producer(i, buffer, capacity).start())
    }

    // multiProdCons(3, 3)


    // notifyAll
    def testNotifyAll(): Unit = {
      val bell = new Object

      (1 to 10).foreach(i => new Thread(() => {
        bell.synchronized{
          println(s"[thread $i] waiting ..")
          bell.wait()
          println(s"[thread $i] hooray!")
        }
      }).start())

      new Thread(() => {
        Thread.sleep(2000)
        println("[announcer] Announcement!")
        bell.synchronized {
          bell.notifyAll() // notifies everyone
          // bell.notify() // notifies just one
        }
      }).start()
    }


    // DEADLOCK

    case class Friend(name: String) {
      def bow(other: Friend): Unit = {
        this.synchronized {
          println(s"$this bowing to $other")
          other.rise(this)
          println(s"$this seeing $other rising")
        }
      }

      def rise(other: Friend): Unit = {
        this.synchronized{
          println(s"$this rising to $other")
        }
      }

      var side = "right"
      def switchSide(): Unit = {
        if(side == "right") side = "left"
        else side = "right"
      }

      def pass(other: Friend): Unit = {
        while(this.side == other.side){
          println(s"$this feel free to pass $other")
          switchSide()
          Thread.sleep(1000)
        }
      }
     }

    val jack = Friend("Jack")
    val pierre = Friend("Pierre")

    // new Thread(() => jack.bow(pierre)).start() // jack locks, then pierre locks
    // new Thread(() => pierre.bow(jack)).start() // pierre locks, then jack locks

    // LIFE-LOCK

     // new Thread(() => jack.pass(pierre)).start()
     // new Thread(() => pierre.pass(jack)).start()

  }
}
