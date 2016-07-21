package edu.knowitall.collection.immutable

import org.junit._
import org.junit.Assert._
import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
object BagSpecTest extends Specification {
  val numbers = (0 until 10).flatMap(n => Iterator.continually(n).take(n)).toList
  val doubled = numbers ::: numbers

  "simple bag checks" in {
    val bag = Bag.from(numbers)
    bag.size must_== (numbers.size)
    bag must containTheSameElementsAs(numbers)

    (bag merge bag) must containTheSameElementsAs(numbers ::: numbers)
    (bag ++ bag) must containTheSameElementsAs(numbers ::: numbers)
    (bag ++ bag).size must_== numbers.size * 2
    (bag ++ numbers) must containTheSameElementsAs(numbers ::: numbers)
    (numbers ++ bag) must containTheSameElementsAs(numbers ::: numbers)

    bag.uniques must contain(numbers.toSet)
  }
}

