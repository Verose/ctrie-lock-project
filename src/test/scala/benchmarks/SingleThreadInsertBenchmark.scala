package benchmarks

import benchmarks.Global._
import org.scalameter.api._
import org.scalameter.picklers.noPickler._


object CTrieSingleThreadInsertBenchmark extends Bench.OfflineReport {
  import ctries2.ConcurrentTrie

  override def reporter: Reporter.Composite[Double] = Reporter.Composite(
    new RegressionReporter(
      RegressionReporter.Tester.Accepter(),
      RegressionReporter.Historian.Window(1)),
    HtmlReporter(embedDsv),
    new LoggingReporter()
  )
  override def executor = SeparateJvmsExecutor(
    new Executor.Warmer.Default,
    Aggregator.average,
    new Measurer.Default)
  override def persistor: Persistor.None.type = Persistor.None

  var ct = new ConcurrentTrie[Elem, Elem]
  val runs: Gen[Int] = Gen.single("run")(rep)

  override def defaultConfig: Context = Context(
    exec.minWarmupRuns -> minWarmupRuns,            // minimum num of warmups
    exec.benchRuns -> benchRuns,                    // desired num of measurements
    exec.independentSamples -> independentSamples,   // number of JVM instances
    exec.jvmflags -> jvmParams,                     // JVM params, used to limit reserved space (default: 2GB)
    reports.resultDir -> "target/benchmarks"
  )

  performance of "SingleThread" in {
    measure method "Insert" in {
      using(runs) setUp { _ =>
        ct = new ConcurrentTrie[Elem, Elem]
      } beforeTests {
        if(debug) println("Starting test Insert")
      } afterTests {
        if(debug) println("Finished Insert")
      } in { _ =>
        var i = 0
        val until = sz
        val e = elems
        while (i < until) {
          ct.update(e(i), e(i))
          i += 1
        }
      }
    }
  }
}

object CTrieLockSingleThreadInsertBenchmark extends Bench.OfflineReport {
  import ctrielock.ConcurrentTrie

  override def reporter: Reporter.Composite[Double] = Reporter.Composite(
    new RegressionReporter(
      RegressionReporter.Tester.Accepter(),
      RegressionReporter.Historian.Window(1)),
    HtmlReporter(embedDsv),
    new LoggingReporter()
  )
  override def executor = SeparateJvmsExecutor(
    new Executor.Warmer.Default,
    Aggregator.average,
    new Measurer.Default)
  override def persistor: Persistor.None.type = Persistor.None

  var ct = new ConcurrentTrie[Elem, Elem]
  val runs: Gen[Int] = Gen.single("run")(rep)

  override def defaultConfig: Context = Context(
    exec.minWarmupRuns -> minWarmupRuns,            // minimum num of warmups
    exec.benchRuns -> benchRuns,                    // desired num of measurements
    exec.independentSamples -> independentSamples,  // number of JVM instances
    exec.jvmflags -> jvmParams,                     // JVM params, used to limit reserved space (default: 2GB)
    reports.resultDir -> "target/benchmarks"
  )

  performance of "SingleThread" in {
    measure method "Insert" in {
      using(runs) setUp { _ =>
        ct = new ConcurrentTrie[Elem, Elem]
      } beforeTests {
        if(debug) println("Starting test Insert")
      } afterTests {
        if(debug) println("Finished Insert")
      } in { _ =>
        var i = 0
        val until = sz
        val e = elems
        while (i < until) {
          ct.update(e(i), e(i))
          i += 1
        }
      }
    }
  }
}

