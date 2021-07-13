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

import de.codecentric.fpl.datatypes.FplInteger;
import de.codecentric.fpl.datatypes.FplValue;
import de.codecentric.fpl.datatypes.list.FplList;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MapAndFlatMap {

	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	public FplList preparedList;
	
	private Random rnd;
	
	@Setup
	public void setup() {
		preparedList = FplList.fromIterator(new Iterator<FplValue>() {
			int i = 0;
			
			@Override
			public FplValue next() {
				return FplInteger.valueOf(i++);
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
	public FplList map() {
		return preparedList.map(new Function<FplValue, FplValue>() {
			
			@Override
			public FplValue apply(FplValue t) {
				return FplInteger.valueOf(((FplInteger)t).getValue() * 2);
			}
		});
	}
	
	@Benchmark
	public FplList flatMap() {
		return preparedList.flatMap(new Function<FplValue, FplList>() {
			
			@Override
			public FplList apply(FplValue t) {
				long v = ((FplInteger)t).getValue();
				int size = rnd.nextInt(5);
				FplValue[] values = new FplValue[size];
				for (int i = 0; i < size; i++) {
					values[i] = FplInteger.valueOf(v + i);
				}
				return FplList.fromValues(values);
			}
		});
	}
}
