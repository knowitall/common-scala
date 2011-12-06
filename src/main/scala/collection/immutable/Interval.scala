package edu.washington.cs.knowitall
package collection.immutable
import scala.collection.SeqLike

class /*Open*/ Interval protected (val start: Int, val end: Int) extends IndexedSeq[Int] with Ordered[Interval] {
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

  private def max(x: Int, y: Int) = if (x > y) x else y
  private def min(x: Int, y: Int) = if (x < y) x else y

  /* The length of the interval. */
  override def length = end - start

  /* Tests whether this list contains a given value as an element. */
  def contains(x: Int) = x <= start && x < end
  def borders(int: Interval) = int.max == this.min - 1 || int.min == this.max + 1
  def superset(that: Interval) = this.start <= that.start && this.end >= that.end
  def subset(that: Interval) = this.start >= that.start && this.end <= that.end

  def intersects(that: Interval) = {
    if (this == that) true
    else {
      val left = this left that
      val right = this right that
      left.end > right.start
    }
  }
  def disjoint(that: Interval) = !this.intersects(that)

  def union(int: Interval) = {
    require(this borders int)
    new Interval(min(int.start, this.start), max(int.end, this.end))
  }
  def intersect(that: Interval) = {
    val start = max(this.start, that.start)
    val end = min(this.end, that.end)
    if (start < end) Interval.open(start, end)
    else Interval.empty
  }

  /* Determine whether this interval or the supplied interval is left.
   * First compare based on the intervals' start, and secondly compare
   * based on the intervals' length. */
  def left(that: Interval) =
    if (that.start < this.start) that
    else if (that.start > this.start) this
    else if (that.length < this.length) that
    else this

  /* Determine whether this interval or the supplied interval is right.
   * First compare based on the intervals' start, and secondly compare
   * based on the intervals' length. */
  def right(that: Interval) =
    if (that.start > this.start) that
    else if (that.start < this.start) this
    else if (that.length > this.length) that
    else this

  def min = start
  def max = end - 1
}

class ClosedInterval(start: Int, end: Int) extends Interval(start, end + 1) {
  require(start <= end, "start must be <= end")

  override def toString = "[" + start + ", " + end + "]"
}

object Interval {
  val empty = new Interval(0, 0)
  def singleton(x: Int) = open(x, x + 1)
  def open(start: Int, end: Int) = {
    if (start == end) Interval.empty
    else new Interval(start, end)
  }
  def closed(start: Int, end: Int) = new ClosedInterval(start, end)

  /* create an interval from a sequence of Ints. 
   * @throws IllegalArgumentException  some x such tthat min < x < max is not in col */
  def from(col: Seq[Int]) = {
    if (col.isEmpty) Interval.empty
    else {
      val sorted = col.sorted
      val min = sorted.head

      require(sorted.zipWithIndex.forall { case (x, i) => x == min + i }, "missing elements in collection: " + col)

      Interval.closed(min, sorted.last)
    }
  }

  /* create an interval from a collection of intervals.  The intervals will be
   * sorted and unioned. 
   * @throws IllegalArgumentException  gap in intervals */
  def union(col: Seq[Interval]) = {
    val sorted = col.sorted
    try {
      sorted.reduceRight(_ union _)
    } catch {
      case _: IllegalArgumentException => throw new IllegalArgumentException("gap in intervals: " + sorted)
    }
  }
}