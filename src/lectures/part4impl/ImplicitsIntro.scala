package lectures.part4impl

object ImplicitsIntro extends App {

  val pair = "Peter" -> "555"
  val intPair = 1 -> 2 // implicit method

  // EXAMPLE
  case class Person(name: String) {
    def greet = s"This is my name $name"
  }

  implicit def fromStringToPerson(string: String): Person = Person(string)

  println("Peter".greet) // the compiler looks for anything with implicit annotation and tries to apply

  // there is one method that matches -> no problems

  class A {
    def greet: Int = 2
  }

  //implicit def fromStringToA(string: String): A = new A // makes a problem

  // implicit parameters

  def increment(x: Int)(implicit amount: Int): Int = x + amount
  implicit val defaultAmount = 10

  increment(2)
}
