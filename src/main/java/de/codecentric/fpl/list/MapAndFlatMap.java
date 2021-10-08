package de.codecentric.fpl.list;


import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MapAndFlatMap {

	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	public FplList<Integer> preparedList;
	
	private Random rnd;
	
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
		rnd = new Random(42);
	}
	
	@TearDown
	public void tearDown() {
		preparedList = null;
	}
	
	@Benchmark
	public FplList<Integer> mapElementsToTheirDoubleValue() {
		return preparedList.map(new Function<Integer, Integer>() {
			
			@Override
			public Integer apply(Integer t) {
				return Integer.valueOf(t * 2);
			}
		});
	}
	
	@Benchmark
	public FplList<Integer> flatMap() {
		return preparedList.flatMap(new Function<Integer, FplList<Integer>>() {
			
			@Override
			public FplList<Integer> apply(Integer t) {
				int size = rnd.nextInt(5);
				return FplList.fromIterator(new Iterator<Integer>() {
					int i = 0;
					
					@Override
					public boolean hasNext() {
						return i < size;
					}

					@Override
					public Integer next() {
						return Integer.valueOf(t + i++);
					}
				}, size);
			}
		});
	}
}
