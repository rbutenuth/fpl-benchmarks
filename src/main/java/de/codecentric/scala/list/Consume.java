package de.codecentric.scala.list;

import org.openjdk.jmh.annotations.*;

import org.openjdk.jmh.infra.Blackhole;
import scala.Tuple2;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Consume {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    public List<java.lang.Long> preparedList;

    public int[] shuffle;

    @Setup
    public void setup() {
        preparedList = List$.MODULE$.tabulate(size, x -> (long)x + 1).toList();
        shuffle = new int[size];
        for (int i = 0; i < size; i++) {
            shuffle[i] = i;
        }
        Random rnd = new Random(42);
        for (int i = size; i > 1; i--) {
            int j = rnd.nextInt(i);
            int tmp = shuffle[i-1];
            shuffle[i-1] = shuffle[j];
            shuffle[j] = tmp;
        }
    }

    @Benchmark
    public void getAllSequentially(Blackhole sink) {
        for (int i = 0; i < size; i++) {
            sink.consume(preparedList.apply(i));
        }
    }

    @Benchmark
    public void getAllRandomly(Blackhole sink) {
        for (int i = 0; i < size; i++) {
            sink.consume(preparedList.apply(shuffle[i]));
        }
    }

    @Benchmark
    public void getAllByIterator(Blackhole sink) {
        scala.collection.Iterator<java.lang.Long> iter = preparedList.iterator();
        while (iter.hasNext()) {
            sink.consume(iter.next());
        }
    }

    @Benchmark
    public List<java.lang.Long> mapElementsToThereDoubleValue() {
        return preparedList.map((java.lang.Long i) -> 2  * i);
    }

    @Benchmark
    public long consumeFromStart() {
        long sum = 0L;
        List<java.lang.Long> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.head();
            list = (List<java.lang.Long>)list.drop(1);
        }
        return sum;
    }

    @Benchmark
    public long consumeFromEnd() {
        long sum = 0L;
        List<java.lang.Long> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.last();
            list = (List<java.lang.Long>)list.dropRight(1);
        }
        return sum;
    }

    /**
     * Simulation of a divide and conquer algorithm.
     */
    @Benchmark
    public long recursiveSplit() {
        return recursiveSplit(preparedList);
    }

    private long recursiveSplit(List<java.lang.Long> list) {
        int length = list.length();
        if(length == 0) {
            return 0;
        } else if (length == 1) {
            return list.head();
        } else {
            Tuple2<List<java.lang.Long>, List<java.lang.Long>> tuple2 = list.splitAt(length / 2);
            return recursiveSplit(tuple2._1) + recursiveSplit(tuple2._2);
        }
    }

}
