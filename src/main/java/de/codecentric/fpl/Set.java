package de.codecentric.fpl;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Set {
	
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
	public void setAllSequential(Blackhole sink) {
		for (int i = 0; i < size; i++) {
			sink.consume(preparedList.set(i, Integer.valueOf(-i)));
		}
	}

	@Benchmark
	public void set10PercentSequential(Blackhole sink) {
		for (int i = 0; i < size / 10; i++) {
			sink.consume(preparedList.set(10 * i, Integer.valueOf(-i)));
		}
	}

	@Benchmark
	public void setAllRandom(Blackhole sink) {
		for (int i = 0; i < size; i++) {
			sink.consume(preparedList.set(shuffle[i], Integer.valueOf(-i)));
		}
	}

	@Benchmark
	public void set10PercentRandom(Blackhole sink) {
		for (int i = 0; i < size / 10; i++) {
			sink.consume(preparedList.set(shuffle[i], Integer.valueOf(-i)));
		}
	}
}
