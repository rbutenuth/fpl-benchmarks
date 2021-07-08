package de.codecentric.scala.list;

import org.openjdk.jmh.annotations.*;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;
import scala.collection.immutable.Range$;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Construct {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    @Benchmark
    public List<Integer> appendAtEnd() {
        List<Integer> list = List$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = (List<Integer>)list.$plus$colon(i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> appendAtStart() {
        List<Integer> list = List$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = (List<Integer>)list.appended(i);
        }
        return list;
    }

    /**
     * Simulation of a recursive algorithm which joins small lists to a big one.
     * Sizes are <em>not</em> powers of two.
     */
    @Benchmark
    public List<Integer> appendLists() {
        return appendLists(0, size);
    }

    private List<Integer> appendLists(int start, int size) {
        if(size == 1) {
            return List$.MODULE$.empty().$colon$colon(start);
        } else {
            int leftSize = size / 2;
            int rightSize = size - leftSize;
            return (List<Integer>)appendLists(start, leftSize).concat(appendLists(start + leftSize, rightSize));
        }
    }
}
