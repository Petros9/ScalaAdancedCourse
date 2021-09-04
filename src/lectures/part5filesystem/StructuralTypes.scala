package lectures.part5filesystem

object StructuralTypes extends App {

  // structural types

  type JavaClosable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("closing")
  }

  // ?! // def closeQuietly(closeable: JavaClosable OR HipsterCloseable)

  type UnifiedCloseable = {
    def close(): Unit
  } // STRUCTURAL TYPE

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

  closeQuietly(new JavaClosable {
    override def close(): Unit = ???
  })
  closeQuietly(new HipsterCloseable)


  // TYPE REFINEMENTS

  type AdvancedCloseable = JavaClosable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaClosable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit = advancedCloseable.closeSilently()

  closeShh(new AdvancedCloseable)


  // USING STRUCTURAL TYPES AS STANDALONE TYPES

  def altClose(closeable: {def close(): Unit}): Unit = closeable.close() // this thing is it's own type

  // type-checking => duck typing

  type SoundMaker = {
  def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static type typing

  // CAVEAT: based on reflection
}
