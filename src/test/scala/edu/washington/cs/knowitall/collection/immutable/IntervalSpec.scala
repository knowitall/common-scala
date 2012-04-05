package edu.washington.cs.knowitall.collection.immutable

import org.junit.runner.RunWith
import org.specs.runner.JUnit4
import org.specs.Specification
import org.specs.runner.JUnitSuiteRunner

@RunWith(classOf[JUnitSuiteRunner])
class IntervalSpecTest extends JUnit4(IntervalSpec)
object IntervalSpec extends Specification {
  "intervals should border each other" in {
    (Interval.open(0, 4) borders Interval.open(4, 8)) must beTrue
    (Interval.open(4, 8) borders Interval.open(0, 4)) must beTrue
    (Interval.open(0, 3) borders Interval.open(4, 8)) must beFalse
    (Interval.open(4, 8) borders Interval.open(0, 3)) must beFalse
    (Interval.empty borders Interval.open(4, 8)) must beFalse
  }

  "intervals should union properly" in {
    (Interval.open(0, 4) union Interval.open(4, 8)) must_== (Interval.open(0, 8))
    (Interval.open(0, 4) union Interval.open(6, 8)) must throwA[IllegalArgumentException]
  }

  "intervals should intersect properly" in {
    (Interval.open(0, 4) intersect Interval.open(4, 8)) must_== (Interval.empty)
    (Interval.open(0, 4) intersect Interval.open(6, 8)) must_== (Interval.empty)
    (Interval.open(0, 4) intersect Interval.open(2, 6)) must_== (Interval.open(2, 4))
  }

  "the correct left interval is determined" in {
    (Interval.open(0, 4) left Interval.open(4, 8)) must_== (Interval.open(0, 4))
    (Interval.open(0, 4) left Interval.open(2, 6)) must_== (Interval.open(0, 4))
    (Interval.open(4, 8) left Interval.open(0, 4)) must_== (Interval.open(0, 4))
    (Interval.open(2, 6) left Interval.open(0, 4)) must_== (Interval.open(0, 4))
  }

  "the correct right interval is determined" in {
    (Interval.open(0, 4) right Interval.open(4, 8)) must_== (Interval.open(4, 8))
    (Interval.open(0, 4) right Interval.open(2, 6)) must_== (Interval.open(2, 6))
    (Interval.open(4, 8) right Interval.open(0, 4)) must_== (Interval.open(4, 8))
    (Interval.open(2, 6) right Interval.open(0, 4)) must_== (Interval.open(2, 6))
  }

  "overlapping intervals have distance 0" in {
    (Interval.open(0, 4) distance Interval.open(2, 6)) must_== (0)
    (Interval.open(2, 6) distance Interval.open(0, 3)) must_== (0)
  }

  "intervals have the correct distance" in {
    (Interval.open(0, 2) distance Interval.open(2, 5)) must_== (1)
    (Interval.open(0, 2) distance Interval.open(3, 5)) must_== (2)
    (Interval.open(0, 2) distance Interval.open(4, 6)) must_== (3)
  }

  "adjacent intervals have the empty set between them" in {
    Interval.between(Interval.open(0, 2), Interval.open(2, 3)) must_== (Interval.empty)
  }

  "between works properly" in {
    Interval.between(Interval.open(0, 2), Interval.open(3, 10)) must_== (Interval.open(2, 3))
    Interval.between(Interval.open(0, 2), Interval.open(6, 10)) must_== (Interval.open(2, 6))
  }

  "empty works properly" in {
    (Interval.empty union Interval.open(2, 4)) must_== (Interval.open(2, 4))
    (Interval.empty intersect Interval.open(2, 4)) must_== (Interval.empty)

    (Interval.empty left Interval.open(2, 4)) must_== (Interval.open(2, 4))
    (Interval.open(2, 4) left Interval.empty) must_== (Interval.open(2, 4))
    (Interval.empty right Interval.open(2, 4)) must_== (Interval.open(2, 4))
    (Interval.open(2, 4) right Interval.empty) must_== (Interval.open(2, 4))

    (Interval.open(2, 4) subset Interval.empty) must beFalse
    (Interval.empty subset Interval.open(2, 4)) must beTrue

    (Interval.open(2, 4) superset Interval.empty) must beTrue
    (Interval.empty superset Interval.open(2, 4)) must beFalse
  }
}
