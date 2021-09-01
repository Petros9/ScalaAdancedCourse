package lectures.part3conc

class ParallelUtils extends App {
  // 1 - parallel collections

  // val parList = List(1, 2, 3).par // par method

  // val aParVector = ParVector[Int](1, 2, 3)

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }
  val list = (1 to 100000).toList
  val serialTime = measure {
    list.map(_ + 1)
  }
  val parallelTime = measure{
    // list.par.map(_ + 1)
  }

  // map reduce model -> splits the elements into chunks, operation, recombine

  // map, flatMap, filter, foreach, reduce, fold

  println(List(1, 2, 3).reduce(_ - _))

  // sometimes you need synchronization on the results
  // List(1, 2, 3).par.foreach(sum += _)

  // configuring
    // .taskSupport = new ForkJoinTaskSupport(new ForkJoinPool(2))


  // atomic operations

  // val atomic = new Atomic[Int](2)
  // atomic.get atomic.set are thread safe
}
