package edu.washington.cs.knowitall
package common
package pimp

import org.junit._
import org.junit.Assert._

class IterableTest {
  @Test def interleave1() = {
    val x = List(1, 3, 5, 7)
    val y = List(2, 4, 6)

    assertEquals(List(1, 2, 3, 4, 5, 6, 7), Iterables.interleave(x, y))
  }

  @Test(expected = classOf[IllegalArgumentException]) def interleave2() = {
    val x = List(2, 4, 6)
    val y = List(1, 3, 5, 7)

    assertEquals(Iterables.interleave(x, y), List(1, 2, 3, 4, 5, 6, 7))
  }
}
