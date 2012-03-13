package edu.washington.cs.knowitall
package common
package enrich

sealed trait SuperTraversableOnce[T] extends scalaz.PimpedType[TraversableOnce[T]] {
  def histogram: Map[T, Int] = {
    value.foldLeft(Map[T, Int]()) { (m, c) =>
      m.updated(c, m.getOrElse(c, 0) + 1)
    }
  }
}

sealed trait SuperTraversableOncePairInt[T] extends scalaz.PimpedType[TraversableOnce[(T, Int)]] {
  def mergeHistograms: Map[T, Int] = {
    value.foldLeft(Map[T, Int]()) { (m, item) =>
      item match {
        case (x, c) => m.updated(x, m.getOrElse(x, 0) + c)
      }
    }
  }
}

sealed trait SuperTraversableOncePair[T, U] extends scalaz.PimpedType[TraversableOnce[(T, U)]] {
  def toMultiMap: Map[T, List[U]] = {
    value.foldLeft(Map[T, List[U]]().withDefaultValue(List.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (v :: map(k)))
    }
  }

  def toSetMultiMap: Map[T, Set[U]] = {
    value.foldLeft(Map[T, Set[U]]().withDefaultValue(Set.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (map(k) + v))
    }
  }
}

sealed trait SuperTraversableOncePairIterable[T, U] extends scalaz.PimpedType[TraversableOnce[(T, Iterable[U])]] {
  def mergeMultiMaps: Map[T, List[U]] = {
    value.foldLeft(Map[T, List[U]]().withDefaultValue(List.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (map(k) ++ v))
    }
  }

  def mergeSetMultiMaps: Map[T, Set[U]] = {
    value.foldLeft(Map[T, Set[U]]().withDefaultValue(Set.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (map(k) ++ v))
    }
  }
}

object Traversables {
  implicit def traversableOnceTo[T](as: TraversableOnce[T]): SuperTraversableOnce[T] = new SuperTraversableOnce[T] {
    val value = as
  }

  implicit def traversableOncePairIntTo[T](as: TraversableOnce[(T, Int)]): SuperTraversableOncePairInt[T] = new SuperTraversableOncePairInt[T] {
    val value = as
  }

  implicit def traversableOncePairIterable[T, U](as: TraversableOnce[(T, Iterable[U])]): SuperTraversableOncePairIterable[T, U] = new SuperTraversableOncePairIterable[T, U] {
    val value = as
  }

  implicit def traversableOncePairTo[T, U](as: TraversableOnce[(T, U)]): SuperTraversableOncePair[T, U] = new SuperTraversableOncePair[T, U] {
    val value = as
  }
}
