package lectures.part1as

import scala.reflect.runtime.universe.Try

object DarkSugars extends App {

  // 1. methods with single param

  def singleArgMethod(arg: Int): String = s"$arg"

  val description = singleArgMethod {
    // some complex code
    42
  }

  val aTryInstance = Try { // java's try {...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }


  // 2. single abstract method

  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x +1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 // magic

  // example: Runnable

  val aThread = new Thread(new Runnable{
    override def run(): Unit = println("hello, scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, scala"))

  abstract class AnAbstractType {
    def implement: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")


  // 3. :: and #:: methods are special

  val prependedList = 2 :: List(3, 4)

  // 2.::(List(3,4))
  // List(3,4)::(2)
  // ?!
  println(1 :: 2 :: 3 :: List(4, 5))
  println(List(4, 5).::(2).::(1)) // equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  class TeenGirl(name: String){
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet"

  // 5. infix types
  class Composite[A, B]
  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???


  // 6. update() methods, very special like apply()

  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten to anArray.update(2, 7)
  // used in mutable collections


  // 7. setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for 00 encapsulation
    def member:Int = internalMember // "getter"

    def member_=(value: Int): Unit =
      internalMember = value // "setter"
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)

}
