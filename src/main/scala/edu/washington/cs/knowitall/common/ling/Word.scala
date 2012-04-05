package edu.washington.cs.knowitall
package common
package ling

object Word {
  def capitalize(s: String) =
    s(0).toUpper + s.substring(1, s.length)
}
