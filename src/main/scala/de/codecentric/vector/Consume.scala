package de.codecentric.vector

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit;
import scala.util.Random
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Consume {
  
  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  var preparedList: Vector[Int] = _
  var shuffle: Array[Int] = _

  @Setup
  def setup =
    preparedList = Vector.tabulate(size)(x => x + 1)
    shuffle = new Array[Int](size)
    for i <- 0 until size do 
      shuffle(i) = i
        
    val rnd = new Random(42)
    for i <- size to 1 by -1 do
      val j = rnd.nextInt(i)
      val tmp = shuffle(i-1)
      shuffle(i-1) = shuffle(j)
      shuffle(j) = tmp
  end setup  
 

  @Benchmark  
  def getAllRandomly(sink: Blackhole) =
    for i <- 0 until size do
      sink.consume(preparedList(shuffle(i)))

  @Benchmark    
  def getAllByIterator(sink: Blackhole) = 
    val iter = preparedList.iterator
    while iter.hasNext do
      sink.consume(iter.next)

  @Benchmark    
  def mapElementsToTheirDoubleValue(): Vector[Int] =    
    preparedList.map(i => 2 * i)

  @Benchmark  
  def consumeFromStart: Int = 
    preparedList.reduceLeft(_ + _)

  @Benchmark  
  def consumeFromEnd: Int =   
    preparedList.reduceRight(_ + _)

  @Benchmark  
  def recursiveSplit: Int =
    recursiveSplit(preparedList)  

  def recursiveSplit(list: Vector[Int]): Int = 
    val length = list.length
    if length == 0 then
      0
    else if length == 1 then
      list.head
    else   
      val (firstHalf, secondHalf) = list.splitAt(length / 2)
      recursiveSplit(firstHalf) + recursiveSplit(secondHalf)

}
