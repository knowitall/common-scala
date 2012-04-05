package edu.washington.cs.knowitall.collection.mutable

import scala.collection.mutable

/**
 * Patricia Stringrie implementation taken from "Programming in Scala" by Martin Odersky.
 *
 *   www.artima.com/scalazine/articles/scala_collections_architecture3.html
 *
 * I (Michael) was going to write my own implementation but when I looked for an
 * example how how to subclass Map properly I found this implementation.
 */
class PrefixSet
    extends mutable.Set[String]
    with mutable.SetLike[String, PrefixSet] {

  val map = PrefixMap[AnyRef]()

  def contains(key: String) = {
    map.contains(key)
  }

  def +=(elem: String) = { map += (elem -> null); this }
  def -=(elem: String) = { map -= elem; this }
  def iterator = map.iterator.map(_._1)
  override def empty = new PrefixSet

}

object PrefixSet {
  def apply[String](xs: (String, String)*) = {
    new PrefixSet ++ xs
  }
}
