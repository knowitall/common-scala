package edu.washington.cs.knowitall.collection.immutable

import Interval.empty

/**
 * Represents an open interval in the Integers.
 *
 * Intervals are created using the companion object.
 *
 * @author  Michael Schmitz
 */
@SerialVersionUID(1234L)
class /*Open*/ Interval protected (val start: Int, val end: Int)
    extends IndexedSeq[Int] with Ordered[Interval] with scala.Serializable {
  import Interval._
  require(start <= end, "start must be <= end")

  override def toString = "[" + start + ", " + end + ")"
  override def equals(that: Any) = that match {
    // fast comparison for Intervals
    case that: Interval => that.canEqual(this) && that.start == this.start && that.end == this.end
    // slower comparison for Seqs
    case that: IndexedSeq[_] => super.equals(that)
    case _ => false
  }
  override def canEqual(that: Any) = that.isInstanceOf[Interval]
  override def compare(that: Interval) =
    if (this.start > that.start) 1
    else if (this.start < that.start) -1
    else this.length - that.length

  /**
   * Return the ith value of the interval.
   *
   * @param  index  the index to get
   * @return  the ith value of the interval
   */
  override def apply(index: Int): Int = {
    require(index >= 0, "index < 0: " + index)
    require(index < end, "index >= end: " + index + " >= " + end)

    min + index
  }

  override def iterator: Iterator[Int] = {
    new Iterator[Int] {
      var index = start

      def hasNext = index < end
      def next() = {
        val result = index
        index += 1
        result
      }
    }
  }

  override def seq = this

  /** The length of the interval. */
  override def length = end - start

  /**
   * Tests whether this list contains a given value as an element.
   *
   * @param  x  the value to check
   * @return  true if this interval contains `x`
   */
  def contains(x: Int) = x <= start && x < end

  /**
   * Tests whether two intervals border but do not overlap.
   *
   * @param  that  the interval to check
   * @return  true if this interval borders the other interval
   */
  def borders(that: Interval) = {
    if (this == empty || that == empty) false
    else that.max == this.min - 1 || that.min == this.max + 1
  }

  /**
   * Tests whether this interval is a superset of another interval.
   *
   * @param  that  the interval to check
   * @return  true if `this` is a superset of `that`
   */
  def superset(that: Interval) = {
    if (that == empty) true
    else if (this == empty) false
    else this.start <= that.start && this.end >= that.end
  }

  /**
   * Tests whether this interval is a subsert of another interval.
   *
   * @param  that  the interval to check
   * @return  true if `this` is a subset of `that`
   */
  def subset(that: Interval) = {
    if (that == empty) false
    else if (this == empty) true
    else this.start >= that.start && this.end <= that.end
  }

  /**
   * Tests whether another interval intersects this interval.
   *
   * @param  that  the interval to check
   * @return  true if `this` intersects `that`
   */
  def intersects(that: Interval) = {
    if (that == empty || this == empty) false
    else if (this == that) true
    else {
      val left = this left that
      val right = this right that
      left.end > right.start
    }
  }

  /**
   * Tests whether another interval is disjoint from this interval.
   * This is the opposite of `intersects`.
   *
   * @param  that  the interval to check
   * @return  true if `this` is disjoint from `that`
   */
  def disjoint(that: Interval) = !this.intersects(that)

  /**
   * Measure the distance between two intervals.
   * Bordering intervals have distance 1 and intersecting
   * intervals have distance 0.  The distance is always
   * a positive number.
   *
   * @param  that  the interval to measure against
   * @return  the distance between two intervals.
   */
  def distance(that: Interval) = {
    require(that != empty && this != empty)
    if (this intersects that) 0
    else (this.min max that.min) - (this.max min that.max)
  }

  /**
   * Takes the union of two intervals.
   * The two intervals must border each other.
   */
  def union(that: Interval) = {
    if (that == empty) this
    else if (this == empty) that
    else {
      require(this borders that)
      new Interval(that.start min this.start, that.end max this.end)
    }
  }

  /**
   * Takes the intersection of two intervals, or Interval.empty
   * if they do not intersect.
   */
  def intersect(that: Interval) = {
    if (that == empty || this == empty) Interval.empty
    else {
      val start = this.start max that.start
      val end = this.end min that.end
      if (start < end) Interval.open(start, end)
      else Interval.empty
    }
  }

  /* Determine whether this interval or the supplied interval is left.
   * First compare based on the intervals' start, and secondly compare
   * based on the intervals' length. */
  def left(that: Interval) =
    if (that == empty) this
    else if (this == empty) that
    else if (that.start < this.start) that
    else if (that.start > this.start) this
    else if (that.length < this.length) that
    else this

  /* Determine whether this interval or the supplied interval is right.
   * First compare based on the intervals' start, and secondly compare
   * based on the intervals' length. */
  def right(that: Interval) =
    if (that == empty) this
    else if (this == empty) that
    else if (that.start > this.start) that
    else if (that.start < this.start) this
    else if (that.length > this.length) that
    else this

  /** The minimum index in the interval. */
  def min = start

  /** The maximum index in the interval. */
  def max = end - 1
}

class ClosedInterval(start: Int, end: Int) extends Interval(start, end + 1) {
  require(start <= end, "start must be <= end")

  override def toString = "[" + start + ", " + end + "]"
}

/** An interval that includes only a single index. */
class SingletonInterval(elem: Int) extends Interval(elem, elem + 1) {
  override def toString = "{" + elem + "}"
}

object Interval {
  val empty = new Interval(0, 0)

  /** Create a new singleton interval. */
  def singleton(x: Int): SingletonInterval = new SingletonInterval(x)

  /** Create a new open interval. */
  def open(start: Int, end: Int): Interval = {
    if (start == end) Interval.empty
    else if (end - start == 1) Interval.singleton(start)
    else new Interval(start, end)
  }

  /** Create a new closed interval. */
  def closed(start: Int, end: Int): Interval = {
    require(end >= start)
    if (end == start) Interval.singleton(start)
    else new ClosedInterval(start, end)
  }

  /** Create an open interval that includes all points between the two intervals. */
  def between(x: Interval, y: Interval): Interval = {
    require(!(x intersects y), "intervals may not intersect")
    Interval.open(x.end min y.end, x.start max y.start)
  }

  /**
   * create an interval from a sequence of `Int`s.
   *
   * @throws IllegalArgumentException  some x such that min < x < max is not in col
   */
  def from(col: Seq[Int]): Interval = {
    if (col.isEmpty) Interval.empty
    else {
      val sorted = col.sorted
      val min = sorted.head

      require(sorted.zipWithIndex.forall { case (x, i) => x == min + i }, "missing elements in collection: " + col)

      Interval.closed(min, sorted.last)
    }
  }

  /**
   * create an interval from a collection of intervals.  The intervals will be
   * sorted and unioned.
   *
   * @throws IllegalArgumentException  gap in intervals
   */
  def union(col: Seq[Interval]): Interval = {
    val sorted = col.sorted
    try {
      sorted.reduceRight(_ union _)
    } catch {
      case _: IllegalArgumentException => throw new IllegalArgumentException("gap in intervals: " + sorted)
    }
  }

  /**
   * create the smallest interval that spans a collection of intervals.
   * The intervals will be sorted and unioned.
   *
   * @throws IllegalArgumentException  gap in intervals
   */
  def span(col: Iterable[Interval]): Interval = {
    Interval.open(col.map(_.min).min, col.map(_.max).max + 1)
  }
}
