package de.codecentric.linked.list;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Construct {

	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	@Benchmark
	public SingleLinkedList<Integer> appendAtStart() {
		SingleLinkedList<Integer> list = (SingleLinkedList<Integer>) SingleLinkedList.EMPTY_LIST;
		for (int i = 0; i < size; i++) {
			list = list.addAtStart(Integer.valueOf(i));
		}
		return list;
	}


	@Benchmark
	public SingleLinkedList<Integer> constructFromIterator() {
		return SingleLinkedList.fromIterator(new Iterator<Integer>() {
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
