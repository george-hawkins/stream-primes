package week2

object StreamPrimes extends App {
  trait MyStream[+A] extends Printer {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def filter(p: A => Boolean): MyStream[A] = show("filter") {
      if (isEmpty) this
      else if (p(head)) MyStream.cons(head, tail.filter(p), "filter")
      else tail.filter(p)
    }

    def apply(i: Int): A = show(s"apply($i)") {
      if (i == 0) head
      else tail.apply(i - 1)
    }
  }

  object MyStream {
    def cons[T](hd: T, tl: => MyStream[T], creator: String) = new MyStream[T] {
      override def isEmpty = false
      override def head = hd
      override def tail = show("tail") {
        tl
      }

      override def toString = s"$creator[$head, ?]"

      plainShow("cons")
    }
    val empty = new MyStream[Nothing] {
      override def isEmpty = true
      override def head = throw new NoSuchElementException("empty.head")
      override def tail = throw new NoSuchElementException("empty.tail")
    }
  }

  def streamRange(lo: Int, hi: Int): MyStream[Int] =
    if (lo >= hi) MyStream.empty
    else MyStream.cons(lo, streamRange(lo + 1, hi), "range")

  def isPrime(n: Int): Boolean = (2 until n) forall (n % _ != 0)

  val p = (streamRange(1000, 10000) filter isPrime) apply 1

  println(p)
}
