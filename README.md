# Common-Scala

This is a collection of common code (enrichments, collections, and functions)
that is used by the UW CSE Turing Center in the Scala programming langauge.

For example, this library contains:

## Collections

* Bag/Multiset implementation.
* Simple graph collection.
* Interval collection for intervals within the integers.
* MultiIterable for iterating over multiple collections in parallel.

## Enrichments

* An extention to Traversables to build a histogram of the elements.
* An extention to Iterables to interleave two iterables.

## Functions

* Code to create precision-yield curves and area under the curve.
* A convenience construct for handling resources as in the C# using construct.

## Dependencies

The only dependency is scalaz which is used very moderately.

## Contributors

* Michael Schmitz <http://www.schmitztech.com/>
* Robert Bart (rbart at cs.washington.edu)
