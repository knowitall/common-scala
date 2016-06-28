** **DEPRECATED!** ** Please see https://github.com/allenai/openie-standalone, which has combined multiple projects into a single project that builds on Scala 2.11.

# Common-Scala

This is a collection of common code (enrichments, collections, and functions)
that is used by the UW CSE Turing Center in the Scala programming langauge.

## Using

Add the following as a Maven dependency.

    <groupId>edu.washington.cs.knowitall.common-scala</groupId>
    <artifactId>common-scala_2.9.2</artifactId>
    <version>[1.0.5, )</version>

The best way to find the latest version is to browse [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22edu.washington.cs.knowitall%22).

## Contents

# Collections

* Bag/Multiset implementation.
* Simple graph collection.
* Interval collection for intervals within the integers.
* MultiIterable for iterating over multiple collections in parallel.

# Enrichments

* An extention to Traversables to build a histogram of the elements.
* An extention to Iterables to interleave two iterables.

# Functions

* Code to create precision-yield curves and area under the curve.
* A convenience construct for handling resources as in the C# using construct.

# Dependencies

The only dependency is scalaz which is used very moderately.

# Contributors

* Michael Schmitz <http://www.schmitztech.com/>
* Robert Bart (rbart at cs.washington.edu)
