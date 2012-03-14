package edu.washington.cs.knowitall
package collection.immutable

import scala.collection.mutable.ListBuffer
import scala.collection.generic.CanBuildFrom
import scala.collection.IterableLike

class Bag[T] private (private val bagmap: Map[T, Int], override val size: Int)
    extends Iterable[T] with IterableLike[T, Bag[T]] {
  private def this() = this(Map[T, Int]().withDefaultValue(0), 0)

  def apply(k: T): Int = bagmap(k)

  // override Object
  override def equals(that: Any) = that match {
    case that: Bag[_] => that.bagmap == this.bagmap
    case _ => false
  }
  override def hashCode = bagmap.hashCode

  // override Traversable
  override def iterator =
    if (bagmap.isEmpty) Iterator.empty
    else bagmap map { case (k, v) => Iterator.continually(k).take(v) } reduce (_ ++ _)

  override def newBuilder = Bag.newBuilder[T]

  // conversions
  def toMap = this.bagmap

  def add(k: T, sumand: Int): Bag[T] = {
    val v = bagmap(k) + sumand
    require(v >= 0, "values must be >= 0")
    new Bag[T](bagmap + (k -> v), size + sumand)
  }

  def ++(kvs: Traversable[(T, Int)]) = kvs.foldLeft(this)((bag, kv) => bag + kv)

  def +(kv: (T, Int)): Bag[T] = kv match {
    case (k, v) =>
      this add (k, v)
  }

  def -(kv: (T, Int)): Bag[T] = kv match {
    case (k, v) =>
      this add (k, -v)
  }

  def +(k: T): Bag[T] =
    this add (k, 1)

  def -(k: T): Bag[T] =
    this add (k, -1)

  def update(kv: (T, Int)): Bag[T] = kv match {
    case (k, v) =>
      require(v >= 0, "values must be >= 0")
      new Bag[T](bagmap + kv, size - bagmap(k) + v)
  }

  def removeKey(k: T): Bag[T] =
    new Bag[T](bagmap - k, size - bagmap(k))

  def get(k: T): Option[Int] = {
    bagmap.get(k)
  }
}

object Bag {
  def fromCounts[T](ts: TraversableOnce[(T, Int)]): Bag[T] = {
    ts.foldLeft(Bag.empty[T]) {
      case (bag, (k, v)) =>
        bag.add(k, v)
    }
  }

  def from[T](ts: TraversableOnce[T]): Bag[T] = {
    ts.foldLeft(Bag.empty[T]) { (bag, k) =>
      bag + k
    }
  }

  def apply[T]() = new Bag[T]()
  def apply[T](varargs: T*) = from(varargs)
  def empty[T] = Bag[T]()

  def newCountsBuilder[T] = ListBuffer[(T, Int)]() mapResult fromCounts
  def newBuilder[T] = ListBuffer[T]() mapResult from

  implicit def canBuildFrom[T] =
    new CanBuildFrom[Bag[T], T, Bag[T]] {
      def apply() = newBuilder
      def apply(from: Bag[T]) = newBuilder
    }
}
