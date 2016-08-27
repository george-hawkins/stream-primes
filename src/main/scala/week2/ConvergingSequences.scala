package week2

object ConvergingSequences extends App {
  var total = 0

  def sqrtStream(x: Double): MyStream[Double] = {
    def improve(guess: Double) = {
      total += 1
      (guess + x / guess) / 2
    }
    // `guesses` simply can't be anything other that lazy - if you try val you'll get:
    // "Error:(11, 55) forward reference extends over definition of value guesses"
    // Fpr a more detailed explanation (both of the Scala and Java aspects) see the comment in sqrtStream in:
    // https://github.com/george-hawkins/square-root-stream/blob/master/src/main/java/net/betaengine/SquareRootStream.java
    lazy val guesses: MyStream[Double] = MyStream.cons(1, (guesses map improve), "guesses")
    guesses
  }

  val s = sqrtStream(4)

  // Note: the difference in the length of output of s.tail.tail.tail.tail.tail.tail.tail and that of s.take(7).toList
  // is surprising (even though they both result in the same number of calls to improve).
  println(s.take(7).toList)

  // The crucial trick is that *both* guesses and MyStream.tail are lazy.
  println(total)
}
