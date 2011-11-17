package edu.washington.cs.knowitall
package common

import scala.util

object Random {
  def choose[A](iterable: Iterable[A], random: util.Random): A = {
    assert(!iterable.isEmpty, "iterable must not be empty")
    val iterator = iterable.iterator
    def rec(n: Int, choice: A): A = {
      if (iterator.hasNext) {
        val next = iterator.next
        if (random.nextDouble() * n < 1) rec(n + 1, next)
        else rec(n + 1, choice)
      } else choice
    }

    rec(2, iterator.next)
  }

  def choose[A](set: Set[A], n: Int, random: util.Random): Set[A] = {
    assert(set.size >= n, "set is smaller than n: " + set.size + " < " + n)
    def rec(n: Int, set: Set[A], choices: Set[A]): Set[A] = {
      if (n == 0) choices
      else {
        val choice = choose(set, random)
        rec(n - 1, set - choice, choices + choice)
      }
    }

    rec(n, set, Set())
  }
}
