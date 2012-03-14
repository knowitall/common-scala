package edu.washington.cs.knowitall
package common
package enrich

import edu.washington.cs.knowitall.collection.immutable.Bag

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
  def toListMultimap: Map[T, List[U]] = {
    value.foldLeft(Map[T, List[U]]().withDefaultValue(List.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (v :: map(k)))
    }
  }

  def toSetMultimap: Map[T, Set[U]] = {
    value.foldLeft(Map[T, Set[U]]().withDefaultValue(Set.empty[U])) {
      case (map, (k, v)) =>
        map + (k -> (map(k) + v))
    }
  }

  def toBagMultimap: Map[T, Bag[U]] = {
    value.foldLeft(Map[T, Bag[U]]().withDefaultValue(Bag.empty[U])) {
      case (map, (k, v)) =>
        val bag = map(k)
        map + (k -> (bag + v))
    }
  }
}

sealed trait SuperTraversableOncePairIterable[T, U] extends scalaz.PimpedType[TraversableOnce[(T, Iterable[U])]] {
  def mergeListMultimaps: Map[T, List[U]] = {
    value.foldLeft(Map[T, List[U]]().withDefaultValue(List.empty[U])) {
      case (map, (k, vs)) =>
        map + (k -> (map(k) ++ vs))
    }
  }

  def mergeSetMultimaps: Map[T, Set[U]] = {
    value.foldLeft(Map[T, Set[U]]().withDefaultValue(Set.empty[U])) {
      case (map, (k, vs)) =>
        map + (k -> (map(k) ++ vs))
    }
  }

  def mergeBagMultimaps: Map[T, Bag[U]] = {
    value.foldLeft(Map[T, Bag[U]]().withDefaultValue(Bag.empty[U])) {
      case (map, (k, vs)) =>
        val bag = map(k)
        map + (k -> (bag ++ vs))
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
