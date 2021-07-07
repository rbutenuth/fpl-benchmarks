# fpl-benchmarks

Benchmarks for fpl project

## Options for a fast (less precise) run

-i 1 one measurement iteration
-opi operations per iteration
-wi 1 one warmup iteration
-f 1 one fork

java -jar target/benchmarks.jar -i 1 -wi 1 -f 1 -bm SingleShotTime
