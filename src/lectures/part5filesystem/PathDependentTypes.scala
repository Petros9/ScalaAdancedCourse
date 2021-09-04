package lectures.part5filesystem

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)

    def printGeneral(i: Outer#Inner): Unit = println(i)
  }


  def aMethod: Int = {
    class HelperClass
    type HelperType = String // compiler requires to declare the type
    2
  }

  val outer = new Outer
  val inner = new outer.Inner // outer.Inner is a TYPE

  val oo = new Outer // ok
  // val otherInner: oo.Inner = new outer.Inner // two different types not ok

  outer.print(inner) // ok
  // oo.print(inner) // not ok

  outer.printGeneral(inner) // ok
  oo.printGeneral(inner) // ok


  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike{
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("home") // ok
  // get[IntItem]("scala") // not ok
}
