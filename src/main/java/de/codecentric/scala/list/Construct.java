package de.codecentric.scala.list;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import scala.collection.Seq;
import scala.collection.immutable.List;
import scala.collection.immutable.List$;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Construct {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    @Benchmark
    public Seq<java.lang.Long> appendAtEnd() {
        Seq<java.lang.Long> list = List$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = list.$plus$colon((long)i);
        }
        return list;
    }

    @Benchmark
    public Seq<java.lang.Long> appendAtStart() {
        Seq<java.lang.Long> list = List$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = list.appended((long)i);
        }
        return list;
    }

    /**
     * Simulation of a recursive algorithm which joins small lists to a big one.
     * Sizes are <em>not</em> powers of two.
     */
    @Benchmark
    public Seq<java.lang.Long> appendLists() {
        return appendLists(0, size);
    }

    private Seq<java.lang.Long> appendLists(int start, int size) {
        if(size == 1) {
            return List$.MODULE$.empty().$colon$colon((long)start);
        } else {
            int leftSize = size / 2;
            int rightSize = size - leftSize;
            return appendLists(start, leftSize).concat(appendLists(start + leftSize, rightSize));
        }
    }
    
    @Benchmark
	public List<java.lang.Long> constructFromIteratorWithKnownSize() {
        return (List<java.lang.Long>) List$.MODULE$.iterate((long)0, size, x -> (long)x + 1);
	}

	//TODO @Benchmark
	public List<java.lang.Long> constructFromIterator() {
        return (List<java.lang.Long>) List$.MODULE$.tabulate(size, x -> (java.lang.Integer)x + 1L);

	}
}
