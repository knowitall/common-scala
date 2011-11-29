package edu.washington.cs.knowitall
package common
package enrich

object Iterables {
  def interleave[T](x: Iterable[_ <: T], y: Iterable[_ <: T]): Seq[T] = {
    val xIt = x.iterator
    val yIt = y.iterator

    var result: List[T] = List[T]()
    while (!xIt.isEmpty && !yIt.isEmpty) {
      result ::= xIt.next()
      result ::= yIt.next()
    }

    if (xIt.hasNext) {
      result ::= xIt.next()
    }

    if (xIt.hasNext || yIt.hasNext) {
      throw new IllegalArgumentException("can't interleave")
    }

    result.reverse
  }
}
