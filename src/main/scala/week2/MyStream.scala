package week2

trait MyStream[+A] extends Printer {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def filter(p: A => Boolean): MyStream[A] = show("filter") {
    if (isEmpty) this
    else if (p(head)) MyStream.cons(head, tail.filter(p), "filter")
    else tail.filter(p)
  }

  def map[R](f: A => R): MyStream[R] = show("map") {
    if (isEmpty) MyStream.empty
    else MyStream.cons(f(head), tail.map(f), "map")
  }

  def take(count: Int): MyStream[A] = show("take") {
    if (isEmpty) this
    else if (count == 0) MyStream.empty
    else MyStream.cons(head, tail.take(count - 1), "take")
  }

  def apply(i: Int): A = show(s"apply($i)") {
    if (i == 0) head
    else tail.apply(i - 1)
  }

  def toList: List[A] = show("toList") {
    if (isEmpty) Nil
    else head :: tail.toList
  }
}

object MyStream {
  def cons[T](hd: T, tl: => MyStream[T], creator: String) = new MyStream[T] {
    override def isEmpty = false
    override def head = hd
    override lazy val tail = show("tail") {
      tl
    }

    override def toString = s"$creator[$head, ?]"

    plainShow("cons")
  }
  val empty = new MyStream[Nothing] {
    override def isEmpty = true
    override def head = throw new NoSuchElementException("empty.head")
    override def tail = throw new NoSuchElementException("empty.tail")

    override def toString = "empty"
  }
}
