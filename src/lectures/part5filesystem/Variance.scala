package lectures.part5filesystem

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // "inheritance" - type substitution of generics

  class Cage[T]
  // yes - covariance

  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class iCage[T]

  // val icage: iCage[Animal] = new iCage[Cat]
  // val x: Int = "hello"

  // no no no - contravariance

  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]
  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T)

  // class ContravariantCage[-T](val animal: T)

  // class ContravariantVariableCage[-T](var animal: T)

  class InvariantVariableCage[T](var animal: T)


  //trait AnotherCovariantCage[+T]{
  //  def addAnimal(animal: T) // contravariant position
  //}

  class AnotherContravariantCage[-T]{
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }
  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog) // is possible


  class PetShop[-T] {
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  // METHODS ARGUMENTS ARE IN CONTRAVARIANT POSITION
  // RETURN TYPES ARE IN COVARIANT POSITIONS


  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???

    def checkVehicles(condition: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicles: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(condition: String): List[T] = ???
    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]){
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](condition: String): List[S] = ???
    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ???
  }
}
