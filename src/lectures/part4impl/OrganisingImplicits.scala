package lectures.part4impl

object OrganisingImplicits extends App {

  println(List(1, 2, 3, 4, 2, 1).sorted)

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  // scala.Predef -> package which is automatically imported while writing code

  /*
  Implicits (used as implicit parameters):
    - val/var
    - object
    - accessor methods
   */

  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Peter", 11),
    Person("Mike", 43)
  )
  implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  println(persons.sorted)

  /*
  Implicit scope
  - normal scope = LOCAL SCOPE
  - imported scope
  - companions of all types involved: List, Ordering, all types involved
   */

  object AlphabeticNameOrdering {
    implicit val alphabeticalOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AgeOrdering._
  println(persons.sorted(ageOrdering))


  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits < b.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }

}
