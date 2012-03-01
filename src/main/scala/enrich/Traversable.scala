package edu.washington.cs.knowitall
package common
package enrich

sealed trait SuperTraversableOnce[T] extends scalaz.PimpedType[TraversableOnce[T]] {
  def histogram: Map[T, Int] = {
    value.foldLeft(Map[T, Int]()) { (m, c) ⇒
      m.updated(c, m.getOrElse(c, 0) + 1)
    }
  }
}

object Traversables {
  implicit def traversableTo[A](as: TraversableOnce[A]): SuperTraversableOnce[A] = new SuperTraversableOnce[A] {
    val value = as
  }
}
