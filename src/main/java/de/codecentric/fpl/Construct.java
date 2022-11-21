package de.codecentric.fpl;

import java.util.Iterator;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.*;
import de.codecentric.fpl.datatypes.list.FplList;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Construct {

	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	@Benchmark
	public FplList<Integer> appendAtEnd() {
		FplList<Integer> list = (FplList<Integer>) FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtEnd(Integer.valueOf(i));
		}
		return list;
	}

	@Benchmark
	public FplList<Integer> appendAtStart() {
		FplList<Integer> list = (FplList<Integer>) FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtStart(Integer.valueOf(i));
		}
		return list;
	}

	/**
	 * Simulation of a recursive algorithm which joins small lists to a big one.
	 * Sizes are <em>not</em> powers of two.
	 */
	@Benchmark
	public FplList<Integer> appendLists() {
		return appendLists(0, size);
	}

	private FplList<Integer> appendLists(int start, int size) {
		if (size == 1) {
			return FplList.fromValue(Integer.valueOf(start));
		} else {
			int leftSize = size / 2;
			int rightSize = size - leftSize;
			return appendLists(start, leftSize).append(appendLists(start + leftSize, rightSize));
		}
	}

	@Benchmark
	public FplList<Integer> constructFromIteratorWithKnownSize() {
		return FplList.fromIterator(new Iterator<Integer>() {
			int i = 0;

			@Override
			public Integer next() {
				return Integer.valueOf(i++);
			}

			@Override
			public boolean hasNext() {
				return i < size;
			}
		}, size);
	}

	@Benchmark
	public FplList<Integer> constructFromIterator() {
		return FplList.fromIterator(new Iterator<Integer>() {
			int i = 0;

			@Override
			public Integer next() {
				return Integer.valueOf(i++);
			}

			@Override
			public boolean hasNext() {
				return i < size;
			}
		});
	}

}
