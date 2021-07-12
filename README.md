# fpl-benchmarks

Benchmarks for fpl project

## Options for a fast (less precise) run

```
-i Iterations, default 5
-r Minimum time per iteration, default 10
-wi 1 one warmup iteration
-f 1 one fork

Fastest:
java -jar target/benchmarks.jar -i 1 -wi 1 -r 1 -bm SingleShotTime

Measurement:
java -jar target/benchmarks.jar -i 3 -r 2 -bm avgt -rf csv -rff result.csv >result.txt
ETA: 13:13
```



JMH Visual Chart: http://deepoove.com/jmh-visual-chart/
JMH Visualizer: https://jmh.morethan.io/

Using JMH with Clojure: http://clojure-goes-fast.com/blog/using-jmh-with-clojure-part1/
