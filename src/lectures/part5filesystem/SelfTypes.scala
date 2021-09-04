package lectures.part5filesystem

object SelfTypes extends App {

  // requires a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // SELF-TYPE whoever implements Singer should implement Instrumentalist as well

    // rest of implementation
    def sing(): Unit
  }
  class LeadSinger extends Singer with Instrumentalist{
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  //class Vocalist extends Singer {
  //  override def sing(): Unit = ???
  //}

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  class A
  class B extends A // B must be an A

  trait T
  trait S {self: T => } // S requires a T

  // CAKE PATTERN => "dependency injection"

  trait ScalaComponent{
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + "something"
  }

  trait ScalaApplication{self: ScalaDependentComponent =>}
  // layer 1 - small components

  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependencies - > PROBLEM
  // class X extends Y
  // class Y extends X

  trait X {self: Y => }// whoever implements X must implement Y and vice versa here
  trait Y {self: X => }
}
