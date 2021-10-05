package de.codecentric.vector

import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Construct {

  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  @Benchmark
  def appendAtEnd: Vector[Int] =
    (0 until size).foldLeft(Vector.empty[Int])((list, i) => list :+ i)
  

  @Benchmark
  def appendAtStart: Vector[Int] = 
    (0 until size).foldLeft(Vector.empty[Int])((list, i) => i +: list)

  @Benchmark  
  def appendLists: Vector[Int] = 
    appendLists(0, size)
  
  def appendLists(start: Int, size: Int): Vector[Int] =
    if size == 1 then
      Vector(start.toInt)
    else 
      val leftSize = size / 2
      val rightSize = size - leftSize
      appendLists(start, leftSize) concat appendLists(start + leftSize, rightSize)
  
  @Benchmark    
  def constructFromIteratorWithKnownSize: Vector[Int] =
    Vector.iterate(0, size)(x => x + 1)    

  @Benchmark  
  def constructFromIterator: Vector[Int] =
    Vector.tabulate(size)(x => x + 1)
  
}
