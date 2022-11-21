package de.codecentric.fpl;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class CopyOfVsNewAndArraycopy {
	
	@Param({"1", "10", "100", "1000", "10000", "100000", "1000000" })
    public int size;
	
	public int[] source;
	
	@Setup
	public void setup() {
		source = new int[size];
        for (int i = 0; i < size; i++) {
        	source[i] = i;
        }
	}
	
	@TearDown
	public void tearDown() {
		source = null;
	}
	
	@Benchmark
	public void withCopyOf(Blackhole sink) {
		sink.consume(copyOf(source, size));
	}

	@Benchmark
	public void withNewAndArrayCopy(Blackhole sink) {
		int[] copy = new int[size];
		arraycopy(source, 0, copy, 0, size);
		sink.consume(copy);
	}
}
