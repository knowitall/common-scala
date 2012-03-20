package edu.washington.cs.knowitall
package collection
package immutable

import org.junit._
import org.junit.Assert._
import org.specs.Specification
import org.specs.runner.JUnit4
import org.junit.runner.RunWith
import org.specs.runner.JUnitSuiteRunner

@RunWith(classOf[JUnitSuiteRunner])
class BagSpecTest extends JUnit4(BagSpec)
object BagSpec extends Specification {
  val numbers = (0 until 10).flatMap(n => Iterator.continually(n).take(n)).toList
  val doubled = numbers ::: numbers

  "simple bag checks" in {
    val bag = Bag.from(numbers)
    bag.size must_== (numbers.size)
    bag must haveTheSameElementsAs(numbers)

    (bag merge bag) must haveTheSameElementsAs(numbers ::: numbers)
    (bag ++ bag) must haveTheSameElementsAs(numbers ::: numbers)
    (bag ++ bag).size must_== numbers.size * 2
    (bag ++ numbers) must haveTheSameElementsAs(numbers ::: numbers)
    (numbers ++ bag) must haveTheSameElementsAs(numbers ::: numbers)

    bag.uniques must haveTheSameElementsAs(numbers.toSet)
  }
}

