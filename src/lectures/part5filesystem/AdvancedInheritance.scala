package lectures.part5filesystem

object AdvancedInheritance extends App {

  // conveniece
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  trait GenericStream[T]{
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem


  trait Animal {def name: String}
  trait Lion extends Animal {
    override def name: String = "lion"
  }
  trait Tiger extends Animal {
    override def name: String = "tiger"
  }
  trait Mutant extends Lion with Tiger {
    override def name: String = "mutant" // may be deleted or not
  }
  // LAST OVERRIDE IS PICKED

  // the super problem + type linearization
  trait Cold {
    def print: Unit = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class White extends Blue with Green {
    override def print: Unit = {
      println("white")
      super.print // super -> another element to the left from "with"
    }
  }
}
