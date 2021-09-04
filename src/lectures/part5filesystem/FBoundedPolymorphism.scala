package lectures.part5filesystem

object FBoundedPolymorphism extends App {

  // trait Animal {
  //  def breed: List[Animal]
  //}

  //class Cat extends Animal {
  //  override def breed: List[Animal] = ??? // list of cats !!
  // }
  //class Dog extends Animal {
  //  override def breed: List[Animal] = ??? // list of dogs !!
  //}

  // 1 - navie
  //trait Animal {
  //  def breed: List[Animal]
  //}

  //class Cat extends Animal {
  //  override def breed: List[Cat] = ??? // list of cats
  //}
  //class Dog extends Animal {
  //  override def breed: List[Dog] = ??? // list of dogs
  //}

  // 2 - f bounded polymorphism

  //trait Animal1[A <: Animal1[A]] {
  //  def breed: List[Animal1[A]]
  //}

  //class Cat1 extends Animal1[Cat1] {
  //  override def breed: List[Animal1[Cat1]] = ??? // list of cats
  //}
  //class Dog1 extends Animal1[Dog1] {
  //  override def breed: List[Animal1[Dog1]] = ??? // list of dogs
  //}

  // 3 - FBP + self-types

  trait Animal1[A <: Animal1[A]] { self: A =>
    def breed: List[Animal1[A]]
  }

  class Cat1 extends Animal1[Cat1] {
    override def breed: List[Animal1[Cat1]] = ??? // list of cats
  }
  class Dog1 extends Animal1[Dog1] {
    override def breed: List[Animal1[Dog1]] = ??? // list of dogs
  }

  // 4 type classes

  trait Animal
  trait CanBreed[A] {
    def breed(a: A): List[A]
  }

  class Dog extends Animal
  object Dog{
    implicit object DogsCanBreed extends CanBreed[Dog]{
      def breed(a: Dog): List[Dog] = List()
    }
  }
  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }

  val dog = new Dog
  dog.breed
  class Cat extends Animal
  object Cat{
    implicit object CatsCanBreed extends CanBreed[Cat]{
      def breed(a: Cat): List[Cat] = List()
    }
  }

  val cat = new Cat
  //cat.breed


  // 5 - pure type classes


  trait Animal5[A]{
    def breed(a: A): List[A]
  }

  class Dog5
  object Dog5 {
    implicit object DogAnimal extends Animal5[Dog5]{
      override def breed(a: Dog5): List[Dog5] = List()
    }
  }

  implicit class AnimalOps5[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal5[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }
  val dog5 = new Dog5
  //dog5.breed

}

