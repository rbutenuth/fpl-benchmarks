package de.codecentric.scala.vector;

import org.openjdk.jmh.annotations.*;

import scala.collection.Seq;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
import scala.collection.immutable.VectorBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Construct {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    @Benchmark
    public Seq<java.lang.Long> appendAtEnd() {
        Seq<java.lang.Long> list = Vector$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = list.$plus$colon((long)i);
        }
        return list;
    }

    @Benchmark
    public Seq<java.lang.Long> appendAtStart() {
        Seq<java.lang.Long> list = Vector$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = list.$colon$plus(i);
        }
        return list;
    }

    /**
     * Simulation of a recursive algorithm which joins small lists to a big one.
     * Sizes are <em>not</em> powers of two.
     */
    @Benchmark
    public Vector<java.lang.Long> appendLists() {
        return appendLists(0, size);
    }

    private Vector<java.lang.Long> appendLists(int start, int size) {
        if(size == 1) {
            VectorBuilder<java.lang.Long> builder = new VectorBuilder<>();
            builder.$plus$eq((long)start);
            return builder.result();
        } else {
            int leftSize = size / 2;
            int rightSize = size - leftSize;
            return (Vector<java.lang.Long>)appendLists(start, leftSize).concat(appendLists(start + leftSize, rightSize));
        }
    }

	public Vector<java.lang.Long> constructFromIteratorWithKnownSize() {
        return (Vector<java.lang.Long>)Vector$.MODULE$.iterate(0L, size, x -> (long)x + 1);
	}

	@Benchmark
	public Vector<java.lang.Long> constructFromIterator() {
        return (Vector<java.lang.Long>)Vector$.MODULE$.tabulate(size, x -> (java.lang.Integer)x + 1L);
	}
}
