package lectures.part4impl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MagnetPattern extends App {

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive[T: Serializer](message: T): Int
    // lots pf overloads
  }

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int]{
    def apply(): Int ={
      println("ADSAD")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int]{
    def apply(): Int ={
      println("ADSAD")
      42
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1- no more type erasure problems

  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int]{
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PResponse)))

  // 2 - lifting works

  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
  }

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit  class AddString(s: String) extends AddMagnet{
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
}
