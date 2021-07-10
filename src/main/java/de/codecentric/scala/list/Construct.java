package de.codecentric.scala.list;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import scala.collection.immutable.List;
import scala.collection.immutable.List$;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
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
    
    // TODO @Benchmark
	public List<Integer> constructFromIteratorWithKnownSize() {
        return null;
	}

	@Benchmark
	public List<Integer> constructFromIterator() {
		// TODO: Is this with known size or with unknown size?
        return List$.MODULE$.tabulate(size, x -> (Integer)x + 1).iterator().toList();
	}
}
