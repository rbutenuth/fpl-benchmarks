package de.codecentric.list

import java.util.concurrent.TimeUnit;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import scala.language.postfixOps

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
class MapAndFlatMap:

  @Param(Array("1", "10", "100", "1000", "10000"))  
  var size: Int = _
  var preparedList: List[Int] = _
  var rnd: Random = _

  @Setup
  def setup(): Unit =
    preparedList = List.iterate(0, size)(i => i + 1)
    rnd = Random(42)
	
  @TearDown
  def tearDown(): Unit =
    preparedList = null;

  @Benchmark
  def mapElementsToTheirDoubleValue(): List[Int] =
    preparedList.map(_ * 2)

  @Benchmark
  def flatMap(): List[Int] =
    preparedList.flatMap { t => 
      val size = rnd.nextInt(5)
      val values: Array[Int] = Array.ofDim(size)
      for
        i <- 0 until size
      yield values(i) = t + i
      List(values:_*)            
    }

  @Benchmark
  def flatMap2(): List[Int] =
    preparedList.flatMap { t => 
      val size = rnd.nextInt(5)
      List.tabulate(size)( i => i + t) 
    }

  @Benchmark
  def flatMap100(): List[Int] =
    preparedList.flatMap { t => 
      val size = rnd.nextInt(100)
      val values: Array[Int] = Array.ofDim(size)
      for
        i <- 0 until size
      yield values(i) = t + i
      List(values:_*)            
    }

  @Benchmark
  def flatMap2_100(): List[Int] =
    preparedList.flatMap { t => 
      val size = rnd.nextInt(100)
      List.tabulate(size)( i => i + t) 
    }

end MapAndFlatMap