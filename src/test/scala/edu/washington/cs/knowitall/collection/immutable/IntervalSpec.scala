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
class IntervalSpecTest extends JUnit4(IntervalSpec)
object IntervalSpec extends Specification {
  "intervals should border each other" in {
    (Interval.open(0, 4) borders Interval.open(4, 8)) must beTrue
    (Interval.empty borders Interval.open(4, 8)) must beFalse
  }
  
  "intervals should union properly" in {
    (Interval.open(0, 4) union Interval.open(4, 8)) must_==(Interval.open(0, 8))
    (Interval.open(0, 4) union Interval.open(6, 8)) must throwA[IllegalArgumentException]
  }
  
  "intervals should intersect properly" in {
    (Interval.open(0, 4) intersect Interval.open(4, 8)) must_==(Interval.empty)
    (Interval.open(0, 4) intersect Interval.open(6, 8)) must_==(Interval.empty)
    (Interval.open(0, 4) intersect Interval.open(2, 6)) must_==(Interval.open(2, 4))
  }
  
  "the correct left interval is determined" in {
    (Interval.open(0, 4) left Interval.open(4, 8)) must_==(Interval.open(0, 4))
    (Interval.open(0, 4) left Interval.open(2, 6)) must_==(Interval.open(0, 4))
    (Interval.open(4, 8) left Interval.open(0, 4)) must_==(Interval.open(0, 4))
    (Interval.open(2, 6) left Interval.open(0, 4)) must_==(Interval.open(0, 4))
  }
  
  "the correct right interval is determined" in {
    (Interval.open(0, 4) right Interval.open(4, 8)) must_==(Interval.open(4, 8))
    (Interval.open(0, 4) right Interval.open(2, 6)) must_==(Interval.open(2, 6))
    (Interval.open(4, 8) right Interval.open(0, 4)) must_==(Interval.open(4, 8))
    (Interval.open(2, 6) right Interval.open(0, 4)) must_==(Interval.open(2, 6))
  }
}
