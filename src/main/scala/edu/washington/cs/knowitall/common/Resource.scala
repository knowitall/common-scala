package edu.washington.cs.knowitall.common

object Resource {
  def using[T <: { def close(): Unit }, S](obj: T)(operation: T => S) = {
    val result = try {
      operation(obj)
    } finally {
      obj.close()
    }

    result
  }
}
