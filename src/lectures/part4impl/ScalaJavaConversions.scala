package lectures.part4impl
import java.{util, util => ju}
object ScalaJavaConversions extends App {

  import collection.JavaConverters._

  val javaSet: ju.Set[Int] = new util.HashSet[Int]()

  (1 to 5).foreach(javaSet.add)

  println(javaSet)

  val scalaSet = javaSet.asScala

  import collection.mutable._

  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumberBuffer = numbersBuffer.asJava


  println(juNumberBuffer.asScala eq numbersBuffer) // same type

  val numbers = List(1, 2, 3)
  val juNumbers = numbers.asJava
  val scalaNumbers = juNumbers.asScala
  print(scalaNumbers eq numbers) // false


  class ToScala[T](value: => T) {
    def asScala: T = value
  }
  implicit def asScalaOptional[T](o: ju.Optional[T]): ToScala[Option[T]] = new ToScala[Option[T]](
    if (o.isPresent) Some(o.get()) else None
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala
}
