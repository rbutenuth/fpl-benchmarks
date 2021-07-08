package de.codecentric.scala.list;

import org.openjdk.jmh.annotations.*;

import org.openjdk.jmh.infra.Blackhole;
import scala.Tuple2;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Consume {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    public List<Integer> preparedList;

    public int[] shuffle;

    @Setup
    public void setup() {
        preparedList = List$.MODULE$.tabulate(size, x -> (Integer)x + 1).toList();
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
        scala.collection.Iterator<Integer> iter = preparedList.iterator();
        while (iter.hasNext()) {
            sink.consume(iter.next());
        }
    }

    @Benchmark
    public List<Double> mapElementsToThereDoubleValue(Blackhole sink) {
        return preparedList.map(i -> i * 2.0);
    }

    @Benchmark
    public long consumeFromStart() {
        long sum = 0L;
        List<Integer> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.head();
            list = (List<Integer>)list.drop(1);
        }
        return sum;
    }

    @Benchmark
    public long consumeFromEnd() {
        long sum = 0L;
        List<Integer> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.last();
            list = (List<Integer>)list.dropRight(1);
        }
        return sum;
    }

    /**
     * Simulation of a divide and conquer algorithm.
     */
    @Benchmark
    public long recursiveSplit() {
        return 1L;
    }

    private long recursiveSplit(List<Integer> list) {
        int length = list.length();
        if(length == 0) {
            return 0;
        } else if (length == 1) {
            return list.head();
        } else {
            Tuple2<List<Integer>, List<Integer>> tuple2 = list.splitAt(length / 2);
            return recursiveSplit(tuple2._1) + recursiveSplit(tuple2._2);
        }
    }

}
