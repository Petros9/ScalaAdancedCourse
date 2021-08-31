package lectures.part2fp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int = {
    x => y => x + y
  }

  val add3 = superAdder(3)

  // METHOD !!!
  def curriedAdder(x: Int)(y: Int): Int = x + y
  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)

  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(inc) // .map(x => inc(x)) // ETA-expansion

  // Partial function applications

  val add5 = curriedAdder(5) _ // Int => Int  (_ makes the difference)

  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y


  val add7 = (x: Int) => simpleAddMethod(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_3 = curriedAddMethod(7) _
  val add7_4 = curriedAddMethod(7)(_)
  val add7_5 = simpleAddMethod(7, _: Int)
  val add7_6 = simpleAddFunction(7, _: Int)

  def concatenator(a: String, b: String, c: String) = a + b + c

  def insertName = concatenator("Hello I'm", _: String, ", how are you?")

  println(insertName("Piotr"))

  def curriedFormatter(s: String)(d: Double): String = s.format(d)

  val numbers = List(Math.PI, Math.E, 1, 9.8)

  val simpleFormatter = curriedFormatter("%4.2f") _ // lift

  println(numbers.map(simpleFormatter))



  def byName(n: => Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1
  def method: Int = 42
  def parentMethod(): Int = 42

  byName(23) // ok
  byName(method) // ok
  byName(parentMethod())
  byName(parentMethod) // ok, but be aware ==> byName(parentMathod())
  // byName(() => 42) not ok
  // byName(parentMethod _) // not ok

  // byFunction(45) // not ok
  // byFunction(method) // not ok
  byFunction(parentMethod) // ok, compiler does ETA-expansion
  byFunction(parentMethod _) // also works
}
