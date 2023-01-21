# Demo of performance of virtual threads in Java 19

A demo of virtual threads in Java 19.

Virtual threads are in preview in Java 19.

The objective of the demo is to have a somewhat fair comparison of the performance 
between a fixed thread pool and virtual threads 
when doing blocking work (not CPU intensive work).

The expectation is that virtual threads will have better resource utilization and give higher throughput.

Disclaimer: This is work-in-progress and it is not at all certain that this is a fair comparison.

## Prerequisites

Java 19 needs to be installed.

## Arguments

Arg 1: on/off - determines if virtual threads is on or off
Arg 2: integer - number of executions to run

## How to run with Maven

Without virtual threads:
```
mvn compile exec:java -Dexec.args="off 5000"
```

With virtual threads:
```
mvn compile exec:java -Dexec.args="on 5000"
```

## How to package and run JAR

Package as JAR
```
mvn package
```

Run as JAR without virtual threads
```
java --enable-preview -jar target/vtd-1.0-SNAPSHOT.jar off 5000
```


Run as JAR with virtual threads
```
java --enable-preview -jar target/vtd-1.0-SNAPSHOT.jar on 5000
```
