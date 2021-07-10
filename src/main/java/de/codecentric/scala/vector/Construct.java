package de.codecentric.scala.vector;

import org.openjdk.jmh.annotations.*;

import scala.collection.immutable.List;
import scala.collection.immutable.List$;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Construct {

    @Param({"1", "10", "100", "1000", "10000" })
    public int size;

    @Benchmark
    public Vector<Integer> appendAtEnd() {
        Vector<Integer> list = Vector$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = (Vector<Integer>)list.$plus$colon(i);
        }
        return list;
    }

    @Benchmark
    public Vector<Integer> appendAtStart() {
        Vector<Integer> list = Vector$.MODULE$.empty();
        for (int i = 0; i < size; i++) {
            list = (Vector<Integer>)list.$colon$plus(i);
        }
        return list;
    }

    /**
     * Simulation of a recursive algorithm which joins small lists to a big one.
     * Sizes are <em>not</em> powers of two.
     */
    @Benchmark
    public Vector<Integer> appendLists() {
        return appendLists(0, size);
    }

    private Vector<Integer> appendLists(int start, int size) {
        if(size == 1) {
            return (Vector<Integer>)Vector$.MODULE$.tabulate(1, x -> start);
        } else {
            int leftSize = size / 2;
            int rightSize = size - leftSize;
            return (Vector<Integer>)appendLists(start, leftSize).concat(appendLists(start + leftSize, rightSize));
        }
    }
    
    // TODO @Benchmark
	public Vector<Integer> constructFromIteratorWithKnownSize() {
        return null;
	}

	@Benchmark
	public Vector<Integer> constructFromIterator() {
		// TODO: Is this with known size or with unknown size?
        return Vector$.MODULE$.tabulate(size, x -> (Integer)x + 1).toVector();
	}
}
