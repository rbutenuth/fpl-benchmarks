package de.codecentric.fpl.list;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import de.codecentric.fpl.datatypes.FplInteger;
import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Benchmark)
public class Construct {

	@Param({"1", "10", "100", "1000", "10000", "100000", "1000000" })
    public int size;
	
	@Benchmark
	public Object appendAtEnd() {
		FplList list = FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtEnd(FplInteger.valueOf(i));
		}
		return list;
	}

	@Benchmark
	public Object appendAtStart() {
		FplList list = FplList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtStart(FplInteger.valueOf(i));
		}
		return list;
	}

}
