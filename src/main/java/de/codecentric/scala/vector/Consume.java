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
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Consume {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    public Vector<java.lang.Long> preparedList;

    public int[] shuffle;

    @Setup
    public void setup() {
        preparedList = Vector$.MODULE$.tabulate(size, x -> (java.lang.Long)x + 1).toVector();
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
    public Vector<java.lang.Long> mapElementsToThereDoubleValue() {
        return preparedList.map((java.lang.Long i) -> 2 * i);
    }

    @Benchmark
    public long consumeFromStart() {
        long sum = 0L;
        Vector<java.lang.Long> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.head();
            list = list.drop(1);
        }
        return sum;
    }

    @Benchmark
    public long consumeFromEnd() {
        long sum = 0L;
        Vector<java.lang.Long> list = preparedList;
        while (!list.isEmpty()) {
            sum += list.last();
            list = list.dropRight(1);
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

    private long recursiveSplit(Vector<java.lang.Long> list) {
        int length = list.length();
        if(length == 0) {
            return 0;
        } else if (length == 1) {
            return list.head();
        } else {
            Tuple2<Iterable<java.lang.Long>, Iterable<java.lang.Long>> tuple2 = list.splitAt(length / 2);
            return recursiveSplit(tuple2._1.toVector()) + recursiveSplit(tuple2._2.toVector());
        }
    }

}
