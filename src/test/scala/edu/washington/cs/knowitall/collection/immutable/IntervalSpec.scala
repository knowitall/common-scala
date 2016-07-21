package edu.knowitall.collection.immutable

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.specs2.ScalaCheck
import org.scalacheck.Prop._
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

@RunWith(classOf[JUnitRunner])
object IntervalSpecTest extends Specification with ScalaCheck {
  "intervals" should {
    "border each other" in {
      (Interval.open(0, 4) borders Interval.open(4, 8)) must beTrue
      (Interval.open(4, 8) borders Interval.open(0, 4)) must beTrue
      (Interval.open(0, 3) borders Interval.open(4, 8)) must beFalse
      (Interval.open(4, 8) borders Interval.open(0, 3)) must beFalse
      (Interval.empty borders Interval.open(4, 8)) must beFalse
    }

    "union properly" in {
      (Interval.open(0, 4) union Interval.open(4, 8)) must_== (Interval.open(0, 8))
      (Interval.open(0, 4) union Interval.open(6, 8)) must throwA[IllegalArgumentException]
    }

    "intersect properly" in {
      (Interval.open(0, 4) intersect Interval.open(4, 8)) must_== (Interval.empty)
      (Interval.open(0, 4) intersect Interval.open(6, 8)) must_== (Interval.empty)
      (Interval.open(0, 4) intersect Interval.open(2, 6)) must_== (Interval.open(2, 4))
    }

    "contain properly" in {
      Interval.open(2, 3) contains 0 must_== false
      Interval.open(2, 3) contains 1 must_== false
      Interval.open(2, 3) contains 2 must_== true
      Interval.open(2, 3) contains 3 must_== false
    }

    "shifts ok" in {
      Interval.open(2, 4) shift 2 must_== Interval.open(4, 6)
      Interval.open(2, 4) shift -2 must_== Interval.open(0, 2)
    }
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

  "leftOf works" in {
    (Interval.open(0, 4) leftOf Interval.open(4, 8)) must beTrue
    (Interval.open(0, 4) leftOf Interval.open(2, 6)) must beFalse
    (Interval.open(4, 8) leftOf Interval.open(0, 4)) must beFalse
    (Interval.open(2, 6) leftOf Interval.open(0, 4)) must beFalse
  }

  "rightOf works" in {
    (Interval.open(0, 4) rightOf Interval.open(4, 8)) must beFalse
    (Interval.open(0, 4) rightOf Interval.open(2, 6)) must beFalse
    (Interval.open(4, 8) rightOf Interval.open(0, 4)) must beTrue
    (Interval.open(2, 6) rightOf Interval.open(0, 4)) must beFalse
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

  val intervalGen = for {
    n <- Gen.choose(0, 100)
    m <- Gen.choose(n, 100)
  } yield Interval.open(n, m)

  "Interval.minimal works properly" in {
    implicit def arbInterval: Arbitrary[List[Interval]] = {
      Arbitrary {
        Gen.listOf(intervalGen)
      }
    }

    forAll { (intervals: List[Interval]) =>
      val min = Interval.minimal(intervals)

      // for all points in the original intervals
      // that point must be in the new intervals
      intervals.forall(i => min.exists(_.contains(i)))

      // for all points in one of the new intervals
      // no other interval may contain the same point
      min.forall(interval => !min.exists(other => !(other eq interval) && (other intersects interval)))

      // result is sorted
      min.sorted == min
    }
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

  "string serialization works properly" in {
    Interval.deserialize(Interval.empty.serialize) must_== Interval.empty
// TODO: FIGHT THIS!
//    check { (x: Int) =>
//      val interval = Interval.singleton(x)
//      Interval.deserialize(interval.serialize) must_== interval
//      Interval.closed(x, x) must_== interval
//    }
    forAll { (a: Int, b: Int) =>
      (a < b) ==> {
        val interval = Interval.open(a, b)
        Interval.deserialize(interval.serialize) must_== interval
      }
    }
    check { (a: Int, b: Int) =>
      (a <= b && b < Int.MaxValue) ==> {
        val interval = Interval.closed(a, b)
        Interval.deserialize(interval.serialize) must_== interval
      }
    }
  }
}
