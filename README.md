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

## Options for a detailed run

Measurement:
java -jar target/benchmarks.jar -i 3 -r 2 -bm avgt -rf csv -rff exact-result-java.csv >exact-result-java.txt
ETA: 13:13
```

## Measure mememory consumption

https://github.com/openjdk/jol



JMH Visual Chart: http://deepoove.com/jmh-visual-chart/
JMH Visualizer: https://jmh.morethan.io/

Using JMH with Clojure: http://clojure-goes-fast.com/blog/using-jmh-with-clojure-part1/


[https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Windows.html)

## Run SBT Console 

(Does not work in gitbash on Windows, use cmd instead.)
`sbt`

## Run JMH within SBT Console

`jmh:run -i 1 -wi 1 -r 1 -bm SingleShotTime`
jmh:run  -i 3 -r 2 -bm avgt -rf csv -rff exact-result-scala.csv


## Documentation about Scala Vector

https://github.com/scala/scala/pull/8534
In Scala since 2.13.x

http://docs.scala-lang.org/overviews/collections/concrete-immutable-collection-classes.html#vectors "Scala's Collection Library overview"
(Da steht auch das "effectivly constant"...