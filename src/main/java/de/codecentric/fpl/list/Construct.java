package de.codecentric.fpl.list;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import de.codecentric.fpl.datatypes.FplInteger;
import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Thread)
public class Construct {

	@Param({"1", "10", "100", "1000", "10000", "100000", "1000000" })
    public int size;
	
	@Benchmark
	public FplList appendAtEnd() {
		FplList list = FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtEnd(FplInteger.valueOf(i));
		}
		return list;
	}

	@Benchmark
	public FplList appendAtStart() {
		FplList list = FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtStart(FplInteger.valueOf(i));
		}
		return list;
	}

	/**
	 * Simulation of a recursive algorithm which joins small lists to a big one.
	 * Sizes are <em>not</em> powers of two.
	 */
	@Benchmark
	public FplList appendLists() {
		return appendLists(0, size);
	}

	private FplList appendLists(int start, int size) {
		if (size == 1) {
			return FplList.fromValues(FplInteger.valueOf(start));
		} else {
			int leftSize = size / 2;
			int rightSize = size - leftSize;
			return appendLists(start, leftSize).append(appendLists(start + leftSize, rightSize));
		}
	}
}
