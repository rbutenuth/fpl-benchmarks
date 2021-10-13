package de.codecentric.arrseq

import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

import scala.collection.immutable.ArraySeq

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Construct:

  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  @Benchmark
  def appendAtEnd: ArraySeq[Int] =
    (0 until size).foldLeft(ArraySeq.empty[Int])((list, i) => list :+ i)
     
  @Benchmark
  def appendAtStart: ArraySeq[Int] = 
    (0 until size).foldLeft(ArraySeq.empty[Int])((list, i) => i +: list)

  @Benchmark  
  def appendLists: ArraySeq[Int] = 
    appendLists(0, size)
  
  def appendLists(start: Int, size: Int): ArraySeq[Int] =
    if size == 1 then
      ArraySeq(start.toInt)
    else 
      val leftSize = size / 2
      val rightSize = size - leftSize
      appendLists(start, leftSize) concat appendLists(start + leftSize, rightSize)
  
  @Benchmark    
  def constructFromIteratorWithKnownSize: ArraySeq[Int] =
    ArraySeq.tabulate(size)(_ + 1) 

  @Benchmark  
  def constructFromIterator: ArraySeq[Int] =
    ArraySeq.from(Iterator.from(0).take(size))

end Construct