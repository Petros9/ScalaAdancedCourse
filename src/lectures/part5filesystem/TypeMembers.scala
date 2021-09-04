package lectures.part5filesystem

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???


  // val vat: ac.BoundedAnimal = new Cat // we don't know what bounded animal is
  val pup: ac.SuperBoundedAnimal = new Dog

  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat

  val anotherCat: CatAlias = new Cat

  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int

    override def add(element: Int): MyList = ???
  }

  // ,type
  type CatsType = cat.type // type alias
  val newCat: CatsType = cat // no new elements of this type

  new CatsType


  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // class CustomList(hd: String, tl: CustomList) extends MList {
  //   type A = String
  //  def head: String = hd
  //  def tail: CustomList = tl
  // }

  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head: Int = hd
    def tail: IntList = tl
  }

}
