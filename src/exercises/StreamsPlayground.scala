package exercises

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](elem: B): MyStream[B] // prepend order
  def ++[B >: A](another: => MyStream[B]): MyStream[B]
  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements ouf of this stream
  def takAsList(n: Int): List[A] = take(n).toList()

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if(isEmpty) acc
    else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](elem: B): MyStream[B] = new Cons(elem, this)
  def ++[B >: Nothing](another: => MyStream[B]): MyStream[B] = another
  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this // takes the first n elements ouf of this stream
}

class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl

  def #::[B >: A](elem: B): MyStream[B] = new Cons(elem, this) // prepend order
  def ++[B >: A](another: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ another)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): MyStream[A] = {
    if(predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  def take(n: Int): MyStream[A] = {
    if( n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n - 1))
  }// takes the first n elements ouf of this stream
}
object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new Cons(start, MyStream.from(generator(start))(generator))
}

object StreamsPlayground extends App {

  val naturals = MyStream.from(1)(_ + 1)

  def fibonacci(first: Int, second: Int): MyStream[Int] = new Cons(first, fibonacci(second, first + second))

  println(fibonacci(1,1).take(100).toList())


  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if(numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))
}
