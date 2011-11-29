package edu.washington.cs.knowitall
package common
package enrich

sealed trait SuperTraversable[T] extends scalaz.PimpedType[Traversable[T]] {
  def histogram: Map[T, Int] = {
    value.foldLeft(Map[T, Int]()) { (m, c) ⇒
      m.updated(c, m.getOrElse(c, 0) + 1)
    }
  }
}

object Traversables {
  implicit def TraversableTo[A](as: Traversable[A]): SuperTraversable[A] = new SuperTraversable[A] {
    val value = as
  }
}
