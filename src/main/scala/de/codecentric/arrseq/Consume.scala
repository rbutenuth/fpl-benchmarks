package de.codecentric.arrseq

import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit;
import scala.util.Random
import scala.collection.immutable.ArraySeq
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class Consume:
  
  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int =_

  var preparedList: ArraySeq[Int] = _
  var shuffle: Array[Int] = _

  @Setup
  def setup =
    preparedList = ArraySeq.tabulate(size)(x => x + 1)
    shuffle = Array.ofDim(size)
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
  def consumeFromStart: Int = 
    var sum = 0
    var list = preparedList
    while(list.nonEmpty) {
      sum = sum + list.head
      list = list.drop(1)
    }
    sum

  @Benchmark  
  def consumeFromEnd: Int =   
    var sum = 0
    var list = preparedList
    while(list.nonEmpty) {
      sum = sum + list.last
      list = list.dropRight(1)
    }
    sum

  @Benchmark  
  def recursiveSplit: Int =
    recursiveSplit(preparedList)  

  def recursiveSplit(list: ArraySeq[Int]): Int = 
    val length = list.length
    if length == 0 then
      0
    else if length == 1 then
      list.head
    else   
      val (firstHalf, secondHalf) = list.splitAt(length / 2)
      recursiveSplit(firstHalf) + recursiveSplit(secondHalf)

end Consume
