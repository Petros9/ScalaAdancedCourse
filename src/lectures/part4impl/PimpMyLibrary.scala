package lectures.part4impl

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(value: Int) extends AnyVal { // only one value
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)


    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux (n - 1)
        }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if(n <= 0) List()
        else concatenate(n - 1) ++ list
      concatenate(value)
    }
  }


  new RichInt(42).sqrt

  42.isEven

  // type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._
  3.seconds

  // compiler does not do multiple implicit searches

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string)
    def encrypt(cypherDistance: Int): String =
      string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2)

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionValue = if(3) "OK" else "Something wrong"

  println(aConditionValue) // "Something wrong"
}
