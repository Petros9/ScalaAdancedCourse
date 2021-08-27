package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionVal = if(aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers type for us
  val aCodeBlock = {
    if(aCondition) 54
    else 56
  }

  // Unit -> do not return anything meaningful, but return side effects
  val theUnit = println("hello")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec def factorial(n: Int, acc: Int): Int =
    if(n <= 0) acc
    else factorial(n - 1, n * acc)

  // object-oriented programming

  class Animal(val name: String)
  class Dog(override val name: String) extends Animal(name)
  trait Carnivore{
    def eat(a: Animal): Unit
  }

  class Crocodile(override val name: String) extends Animal(name) with Carnivore {
    override def eat(a: Animal): Unit = println("something")
  }

  val aDog = new Dog("Rex")
  val aCroc = new Crocodile("Steven")
  aCroc.eat(aDog)
  aCroc eat aDog


  println(1.+(2)) // 1 + 2

  // anonymous classes

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Roar")
  }

  // generics
  abstract class MyList[+A] // variance and variance problems

  // singleton objects and companions
  object MyList // class and object are companions

  // case class

  case class Person(name: String, age: Int)


  // exceptions and try/catch/finally

  val throwsException = throw new RuntimeException

  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case _: Exception => "an exception"
  } finally {
    println("some logs")
  }

  // functional programming

  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1

  List(1, 2, 3).map(anonymousIncrementer)


  // map, flatMap, filter

  val pairs = for{
    num <- List(1, 2, 3, 4) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Peter" -> 123,
    "Lop" -> 345
  )

  // collections: Options, Try

  val anOption = Some(2)

  // pattern matching

  val x = 2

  val order = x match{
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)

  val greeting = bob match{
    case Person(n, _) => s"Hi, my name is $n"
  }

  // all the patterns

}
