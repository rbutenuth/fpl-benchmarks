package de.codecentric.linked.list;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import de.codecentric.fpl.datatypes.FplInteger;
import de.codecentric.fpl.datatypes.FplValue;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Construct {

	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	@Benchmark
	public SingleLinkedList appendAtStart() {
		SingleLinkedList list = SingleLinkedList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtStart(FplInteger.valueOf(i));
		}
		return list;
	}


	@Benchmark
	public SingleLinkedList constructFromIterator() {
		return SingleLinkedList.fromIterator(new Iterator<FplValue>() {
			int i = 0;

			@Override
			public FplValue next() {
				return FplInteger.valueOf(i++);
			}

			@Override
			public boolean hasNext() {
				return i < size;
			}
		});
	}

}
