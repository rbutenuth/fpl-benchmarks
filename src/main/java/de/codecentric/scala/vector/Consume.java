package de.codecentric.scala.vector;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.Tuple2;
import scala.collection.Iterable;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Consume {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    public Vector<Integer> preparedList;

    public int[] shuffle;

    @Setup
    public void setup() {
        preparedList = Vector$.MODULE$.tabulate(size, x -> (Integer)x + 1).toVector();
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
    public Vector<Double> mapElementsToThereDoubleValue(Blackhole sink) {
        return (Vector<Double>) preparedList.map(i -> 2.0 * (Integer)i);
    }

    @Benchmark
    public long consumeFromStart() {
        long sum = 0L;
        Vector<Integer> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.head();
            list = (Vector<Integer>)list.drop(1);
        }
        return sum;
    }

    @Benchmark
    public long consumeFromEnd() {
        long sum = 0L;
        Vector<Integer> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.last();
            list = (Vector<Integer>)list.dropRight(1);
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

    private long recursiveSplit(Vector<Integer> list) {
        int length = list.length();
        if(length == 0) {
            return 0;
        } else if (length == 1) {
            return list.head();
        } else {
            Tuple2<Iterable<Integer>, Iterable<Integer>> tuple2 = list.splitAt(length / 2);
            return recursiveSplit(tuple2._1.toVector()) + recursiveSplit(tuple2._2.toVector());
        }
    }

}
