Stream Primes
=============

This project demonstrates the order of operations for the expression:

    ((1000 to 10000).toStream filter isPrime)(1)

I.e. find the second prime in the range 1,000 to 10,000.

You can jump straight to the [Output section](#output) to see the output for the above expression or read on for an explanation of the implementation and of the logging output.

At the end there's also an [Extras section](#extras) that points out how much work occurs to create a filtered stream even if there's no terminal operation consuming values.

**Update:** this project now also includes  [`ConvergingSequences.scala`](src/main/scala/week2/ConvergingSequences.scala) (in addition to [`StreamPrimes.scala`](src/main/scala/week2/StreamPrimes.scala)). This class can be used to demonstrate the affect that making `tail` lazy or not has on working with converging sequences.

Implementation
--------------

This project provides its own stream implementation `MyStream` and rather than creating a stream from a range, as above, it creates a stream directly with its own `streamRange` method.

It does all this so we can log all the steps that occur.

The code for `MyStream` etc. can be seen in [`MyStream.scala`](src/main/scala/week2/MyStream.scala).

You'll see that `MyStream` extends [`Printer`](src/main/scala/week2/Printer.scala) and that some of the methods involve a call to `show(...)`, e.g.:

```scala
override def tail = show("tail") {
  tl
}
```

To eliminate the logging just remove the `extends Printer` and the calls to `plainShow(...)` and `show(...)` so that e.g. the above `tail` definition becomes:

```scala
override def tail = {
  tl
}
// Or just plain `override def tail = tl`.
```

Logging
-------

So what does `show(...)` do? It logs that we entered a function, produces the result of the function, logs that we're exiting the function (including the result in the output) and returns the result.

It's written to ensure that it doesn't affect any call-by-name evaluation that occurs and affects neither the order of evaluation nor the number of times evaluation occurs.

If you leave the logging in and run the program you'll see it logging lines of the form:

    -filter: range[1009, ?] - result = filter[1009, ?]

`+` indicates entering a method and `-` indicates exiting. So the above indicates we're exiting the `filter` method of a cons cell `[1009, ?]`, that was created in the `streamRange` method, and that the result is a _different_ cons cell `[1009, ?]` that was created in the `filter` method.

Cons cells, like `[1009, ?]` are preceded by a string indicating where they were created, i.e. `range` for those created in `streamRange` and `filter` for those created in `filter`.

Unfortunately `filter` appears both as a method name and as a location where cons cells are created.

Output
------

If you leave in the logging and run the program you'll see that the sequence of operations is:

```text
cons: range[1000, ?]
+filter: range[1000, ?]
  +tail: range[1000, ?]
    cons: range[1001, ?]
  -tail: range[1000, ?] - result = range[1001, ?]
  +filter: range[1001, ?]
    +tail: range[1001, ?]
      cons: range[1002, ?]
    -tail: range[1001, ?] - result = range[1002, ?]
    +filter: range[1002, ?]
      +tail: range[1002, ?]
        cons: range[1003, ?]
      -tail: range[1002, ?] - result = range[1003, ?]
      +filter: range[1003, ?]
        +tail: range[1003, ?]
          cons: range[1004, ?]
        -tail: range[1003, ?] - result = range[1004, ?]
        +filter: range[1004, ?]
          +tail: range[1004, ?]
            cons: range[1005, ?]
          -tail: range[1004, ?] - result = range[1005, ?]
          +filter: range[1005, ?]
            +tail: range[1005, ?]
              cons: range[1006, ?]
            -tail: range[1005, ?] - result = range[1006, ?]
            +filter: range[1006, ?]
              +tail: range[1006, ?]
                cons: range[1007, ?]
              -tail: range[1006, ?] - result = range[1007, ?]
              +filter: range[1007, ?]
                +tail: range[1007, ?]
                  cons: range[1008, ?]
                -tail: range[1007, ?] - result = range[1008, ?]
                +filter: range[1008, ?]
                  +tail: range[1008, ?]
                    cons: range[1009, ?]
                  -tail: range[1008, ?] - result = range[1009, ?]
                  +filter: range[1009, ?]
                    cons: filter[1009, ?]
                  -filter: range[1009, ?] - result = filter[1009, ?]
                -filter: range[1008, ?] - result = filter[1009, ?]
              -filter: range[1007, ?] - result = filter[1009, ?]
            -filter: range[1006, ?] - result = filter[1009, ?]
          -filter: range[1005, ?] - result = filter[1009, ?]
        -filter: range[1004, ?] - result = filter[1009, ?]
      -filter: range[1003, ?] - result = filter[1009, ?]
    -filter: range[1002, ?] - result = filter[1009, ?]
  -filter: range[1001, ?] - result = filter[1009, ?]
-filter: range[1000, ?] - result = filter[1009, ?]
+apply(1): filter[1009, ?]
  +tail: filter[1009, ?]
    +tail: range[1009, ?]
      cons: range[1010, ?]
    -tail: range[1009, ?] - result = range[1010, ?]
    +filter: range[1010, ?]
      +tail: range[1010, ?]
        cons: range[1011, ?]
      -tail: range[1010, ?] - result = range[1011, ?]
      +filter: range[1011, ?]
        +tail: range[1011, ?]
          cons: range[1012, ?]
        -tail: range[1011, ?] - result = range[1012, ?]
        +filter: range[1012, ?]
          +tail: range[1012, ?]
            cons: range[1013, ?]
          -tail: range[1012, ?] - result = range[1013, ?]
          +filter: range[1013, ?]
            cons: filter[1013, ?]
          -filter: range[1013, ?] - result = filter[1013, ?]
        -filter: range[1012, ?] - result = filter[1013, ?]
      -filter: range[1011, ?] - result = filter[1013, ?]
    -filter: range[1010, ?] - result = filter[1013, ?]
  -tail: filter[1009, ?] - result = filter[1013, ?]
  +apply(0): filter[1013, ?]
  -apply(0): filter[1013, ?] - result = 1013
-apply(1): filter[1009, ?] - result = 1013
```

Extras
------

Instead of the original expression try running the program without any apply step, i.e. change the definition of `p` to:

    val p = (streamRange(1000, 10000) filter isPrime)

You'll see exactly the same initial output as above except it stops at the line before `+apply(1): ...`.

So even though we've only created a filtered stream, and not consumed a single value, quite a lot of work still had to occur to produce the initial filtered cons cell.
