package edu.washington.cs.knowitall
package common
package enrich

import org.junit.runner.RunWith
import org.specs.runner.JUnit4
import org.specs.Specification
import org.specs.runner.JUnitSuiteRunner

@RunWith(classOf[JUnitSuiteRunner])
class TraversableSpecTest extends JUnit4(TraversableSpec)
object TraversableSpec extends Specification {
  import Traversables._
  
  "simple histogram works fine" in {
    val h1 = List(1, 2, 2, 3, 3, 3).histogram
    val h2 = List(3, 2, 1, 3, 2, 3).histogram
    h1 must_== h2
    h1 must haveTheSameElementsAs(List((1, 1), (2, 2), (3, 3)))
  }

  "histogram from partials works fine" in {
    val h1 = List((1, 1), (2, 2), (2, 2), (3, 3), (3, 3), (3, 3)).histogramFromPartials
    val h2 = List((1, 1), (2, 2), (2, 2), (3, 3), (3, 3), (3, 3)).reverse.histogramFromPartials
    h1 must_== h2
    h1 must haveTheSameElementsAs(List((1, 1), (2, 4), (3, 9)))
  }
}