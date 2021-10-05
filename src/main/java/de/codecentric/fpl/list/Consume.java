package de.codecentric.fpl.list;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Consume {
	
	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	public FplList<Integer> preparedList;
	
	public int[] shuffle;
	
	@Setup
	public void setup() {
		preparedList = FplList.fromIterator(new Iterator<Integer>() {
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
		shuffle = new int[size];
		for (int i = 0; i < size; i++) {
			shuffle[i] = i;
		}
		Random rnd = new Random(42);
        for (int i = size; i > 1; i--) {
        	int j = rnd.nextInt(i);
            int tmp = shuffle[i-1];
            shuffle[i-1] = shuffle[j];
            shuffle[j] = tmp;
        }
	}
	
	@TearDown
	public void tearDown() {
		preparedList = null;
	}
	
	@Benchmark
	public void getAllRandomly(Blackhole sink) {
		for (int i = 0; i < size; i++) {
			sink.consume(preparedList.get(shuffle[i]));
		}
	}

	@Benchmark
	public void getAllByIterator(Blackhole sink) {
		Iterator<Integer> iter = preparedList.iterator();
		while (iter.hasNext()) {
			sink.consume(iter.next());
		}
	}

	@Benchmark
	public int consumeFromStart() {
		int sum = 0;
		FplList<Integer> list = preparedList;
		while (!list.isEmpty()) {
			sum += list.first();
			list = list.removeFirst();
		}
		return sum;
	}
	
	@Benchmark
	public int consumeFromEnd() {
		int sum = 0;
		FplList<Integer> list = preparedList;
		while (!list.isEmpty()) {
			sum += list.last();
			list = list.removeLast();
		}
		return sum;
	}
	
	/**
	 * Simulation of a divide and conquer algorithm.
	 */
	@Benchmark
	public int recursiveSplit() {
		return recursiveSplit(preparedList);
	}

	private int recursiveSplit(FplList<Integer> list) {
		int length = list.size();
		if (length == 0) {
			return 0;
		} else if (length == 1) {
			return list.first();
		} else {
			return recursiveSplit(list.lowerHalf()) + recursiveSplit(list.upperHalf());
		}
	}
}
