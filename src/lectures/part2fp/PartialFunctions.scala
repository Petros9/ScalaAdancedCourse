package lectures.part2fp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int


  val aFussyFunction = (x: Int) =>
    if(x == 1) 42
    else if(x == 2) 56
    else if(x == 5) 123
    else throw new FunctionNotApplicableException


  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 123
  }
  // {1, 2, 5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 123
  } // partial function value

  // partial functions are based on pattern matching

  println(aPartialFunction(2))


  // partial function utilities
  println(aPartialFunction.isDefinedAt(12))

  val lifted = aPartialFunction.lift // Int => Option[Int]

  println(lifted(2)) // Some(56)
  println(lifted(98)) // None

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2)) // 56
  println(pfChain(45)) // 67


  // partial functions extend normal functions

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well

  val aMappedList = List(1, 2, 3).map{
    case 1 => 42
    case 2 => 56
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: Partial Function can only have ONE parameter type
   */

  /**
   * Exercises
   *
   * 1 - construct a PF instance (anonymous class)
   * 2 - chat-bot as a Partial Function
   */
  val aManualFussyFunctions = new PartialFunction[Int, Int] {
    override def apply(value: Int): Int = value match{
      case 1 => 42
      case 2 => 56
      case 5 => 123
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }



  val chatBot: PartialFunction[String, String] = {
    case "hello" => "Hello"
    case "goodbye" => "No"
    case "something" => "QWEQWE!@#!@#"
  }
  scala.io.Source.stdin.getLines().foreach(line => println("chatbot:" + chatBot(line)))

  // scala.io.Source.stdin.getLines().map(chatBot).foreach(println) // works as well
}
