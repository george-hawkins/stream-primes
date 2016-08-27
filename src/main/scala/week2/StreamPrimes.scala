package week2

object StreamPrimes extends App {
  // The definition of MyStream has been moved out into a separate file.

  def streamRange(lo: Int, hi: Int): MyStream[Int] =
    if (lo >= hi) MyStream.empty
    else MyStream.cons(lo, streamRange(lo + 1, hi), "range")

  def isPrime(n: Int): Boolean = (2 until n) forall (n % _ != 0)

  val p = (streamRange(1000, 10000) filter isPrime) apply 1

  println(p)
}
