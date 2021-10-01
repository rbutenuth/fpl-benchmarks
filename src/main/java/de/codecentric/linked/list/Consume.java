package de.codecentric.linked.list;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import de.codecentric.fpl.EvaluationException;
import de.codecentric.fpl.datatypes.FplInteger;
import de.codecentric.fpl.datatypes.FplValue;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Consume {
	
	@Param({"1", "10", "100", "1000", "10000" })
    public int size;
	
	public SingleLinkedList preparedList;
	
	public int[] shuffle;
	
	@Setup
	public void setup() {
		preparedList = SingleLinkedList.fromIterator(new Iterator<FplValue>() {
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
	public void getAllSequentially(Blackhole sink) throws EvaluationException {
		for (int i = 0; i < size; i++) {
			sink.consume(preparedList.get(i));
		}
	}

	@Benchmark
	public void getAllRandomly(Blackhole sink) throws EvaluationException {
		for (int i = 0; i < size; i++) {
			sink.consume(preparedList.get(shuffle[i]));
		}
	}

	@Benchmark
	public void getAllByIterator(Blackhole sink) throws EvaluationException {
		Iterator<FplValue> iter = preparedList.iterator();
		while (iter.hasNext()) {
			sink.consume(iter.next());
		}
	}

	@Benchmark
	public SingleLinkedList mapElementsToTheirDoubleValue(Blackhole sink) throws EvaluationException {
		// The SingleLinkedList has no map operation, but this comes close to it.
		// We make use of the fact that we know the size of the resulting list in advance.
		Iterator<FplValue> iter = preparedList.iterator();
		return SingleLinkedList.fromIterator(new Iterator<FplValue>() {

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public FplInteger next() {
				return FplInteger.valueOf(((FplInteger)iter.next()).getValue() * 2);
			}
		});
	}

	@Benchmark
	public long consumeFromStart() throws EvaluationException {
		long sum = 0L;
		SingleLinkedList list = preparedList;
		while (!list.isEmpty()) {
			sum += ((FplInteger)list.first()).getValue();
			list = list.removeFirst();
		}
		return sum;
	}
	}
