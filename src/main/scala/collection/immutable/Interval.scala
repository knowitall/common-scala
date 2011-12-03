package edu.washington.cs.knowitall
package collection.immutable

class /*Open*/ Interval protected (val start: Int, val end: Int) {
  require(start <= end, "start must be <= end")

  override def toString = "[" + start + ", " + end + ")"
  override def equals(that: Any) = that match {
    case that: Interval => that.canEqual(this) && that.start == this.start && that.end == this.end
    case _ => false
  }
  def canEqual(that: Any) = that.isInstanceOf[Interval]

  private def max(x: Int, y: Int) = if (x > y) x else y
  private def min(x: Int, y: Int) = if (x < y) x else y

  def contains(x: Int) = x <= start && x < end
  def borders(int: Interval) = int.end == this.first - 1 || int.start == this.last + 1
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

  def left(that: Interval) = if (that.start < this.start) that else this
  def right(that: Interval) = if (that.start > this.start) that else this

  def first = start
  def last = end - 1
}

class ClosedInterval(start: Int, end: Int) extends Interval(start, end + 1) {
  require(start <= end, "start must be <= end")

  override def toString = "[" + start + ", " + end + "]"
}

object Interval {
  val empty = new Interval(0, 0)
  def open(start: Int, end: Int) = {
    if (start == end) Interval.empty
    else new Interval(start, end)
  }
  def closed(start: Int, end: Int) = new ClosedInterval(start, end)
}