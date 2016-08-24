package week2

private object Printer {
  def show[T](m: String, that: Any)(expr : => T): T = {
      indent += "  "
      doShow(s"+$m: $that")
      val res = expr
      doShow(s"-$m: $that - result = $res")
      indent = indent.substring(2)
      res
  }

  def plainShow(m: String, that: Any) = doShow(s"  $m: $that")

  private var indent = ""

  private def doShow(s: String): Unit = println(s"$indent$s")
}

trait Printer {
  def show[T](m: String)(expr : => T) = Printer.show(m, this)(expr)

  def plainShow(m: String) = Printer.plainShow(m, this)
}
