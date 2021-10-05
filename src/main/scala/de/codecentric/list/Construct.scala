package de.codecentric.list

import org.openjdk.jmh.annotations._
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Construct {

  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  @Benchmark
  def appendAtEnd: List[Int] =
    (0 until size).foldLeft(List.empty[Int])((list, i) => list :+ i)
  

  @Benchmark
  def appendAtStart: List[Int] = 
    (0 until size).foldLeft(List.empty[Int])((list, i) => i +: list)


  @Benchmark  
  def appendLists: List[Int] = 
    appendLists(0, size)
  
  def appendLists(start: Int, size: Int): List[Int] =
    if size == 1 then
      List(start.toInt)
    else 
      val leftSize = size / 2
      val rightSize = size - leftSize
      appendLists(start, leftSize) concat appendLists(start + leftSize, rightSize)
  
  @Benchmark    
  def constructFromIteratorWithKnownSize: List[Int] =
    List.iterate(0, size)(x => x + 1)    

  @Benchmark  
  def constructFromIterator: List[Int] =
    List.tabulate(size)(x => x + 1)

}
