package lectures.part1as

object AdvancedPatternMatching extends App {
  val numbers = List(1)

  val description = numbers match {
    case head :: Nil => println(s"the only element is $head.")
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if(person.age < 21) None
      else Some((person.name, person.age))


    def unapply(age: Int): Option[String] =
      Some(if(age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 12) // match error

  //val greeting = bob match {
  //  case Person(n, b) => s"Hi, my name is $n and I am $b years old"
  //}

  //println(greeting)
  // create a singleton object with unapply method if you cannot make case class

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  class Number(val value: Int)
  object Number {
    def checkProperties(value: Int): List[String] = {
      var aList = List("")
      if(value < 10) aList = aList :+ "is digit"
      if(value % 2 == 0) aList = aList :+ "an even number"
      if(aList.size == 1) aList = aList :+ "no property"
      aList
    }
    def unapply(value: Int): Option[List[String]] = {
      Some(checkProperties(value))
    }
  }

  val number2 = new Number(2)
  val number11 = new Number(11)
  val properties2 = number2.value match {
    case Number(properties) => properties.foreach(property => println(property))
  }
  val properties11 = number11.value match {
    case Number(properties) => properties.foreach(property => println(property))
  }

  object even {
    def unapply(arg: Int): Boolean =
      arg % 2 == 0
  }

  object digit {
    def unapply(arg: Int): Boolean =
      0 < arg && arg < 10
  }
  val n: Int = 45
  val mathProperty = n match {
    case digit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }


  // infix patterns

  case class Or[A, B](a: A, b: B) // Either
  val either = Or(2, "two")

  val humanDescription = either match {
    case number Or string => s"$number is written as $string" // Or(number, string)
  }

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList{
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if(list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }


  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))

  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty,: Boolean, get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String]{
      def isEmpty = false
      def get: String = person.name
    }
  }

  println(bob match{
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  })
}
