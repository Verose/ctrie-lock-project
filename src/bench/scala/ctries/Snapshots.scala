package ctries



import Global._
import scala.testing.Benchmark



object SingleRemovingCtrie2 extends Benchmark {
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  
  override def setUp() {
    for (i <- 0 until sz) ct.put(elems(i), elems(i))
  }
  
  def run() {
    for (i <- 0 until sz) ct.remove(elems(i))
  }
  
}


object SingleRemovingCtrie2Snapshot extends Benchmark {
  import ctries2.ConcurrentTrie
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  for (i <- 0 until sz) ct.put(elems(i), elems(i))
  
  def run() {
    val snap = ct.snapshot()
    for (i <- 0 until sz) snap.remove(elems(i))
  }
  
}


object SingleInsertionCtrie2 extends Benchmark {
  
  var ct = new ctries2.ConcurrentTrie[Elem, Elem]
  
  override def setUp() {
    ct = new ctries2.ConcurrentTrie[Elem, Elem]
    for (i <- 0 until sz) ct.update(elems(i), elems(i))
  }
  
  def run() {
    for (i <- 0 until sz) ct.update(elems(i), elems(i))
  }
}


object SingleInsertionCtrie2Snapshot extends Benchmark {
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  for (i <- 0 until sz) ct.update(elems(i), elems(i))
  
  def run() {
    val snap = ct.snapshot()
    for (i <- 0 until sz) snap.update(elems(i), elems(i))
  }
}


object MultiRemovingCtrie2 extends Benchmark {
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  
  override def setUp() {
    for (i <- 0 until sz) ct.update(elems(i), elems(i))
  }
  
  def run() {
    val p = par.get
    val step = sz / p
    
    val ins = for (i <- 0 until p) yield new Remover(ct, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Remover(ct: ctries2.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      
      while (i < until) {
        ct.remove(e(i))
        i += 1
      }
    }
  }
}


object MultiRemovingCtrie2Snapshot extends Benchmark {
  import ctries2.ConcurrentTrie
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  
  override def setUp() {
    for (i <- 0 until sz) ct.update(elems(i), elems(i))
  }
  
  def run() {
    val p = par.get
    val step = sz / p
    val snap = ct.snapshot()
    
    val ins = for (i <- 0 until p) yield new Remover(snap, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Remover(snap: ctries2.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      
      while (i < until) {
        snap.remove(e(i))
        i += 1
      }
    }
  }
}


object MultiLookingCtrie2 extends Benchmark {
  import ctries2.ConcurrentTrie
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  for (i <- 0 until sz) ct.update(elems(i), elems(i))
  
  def run() {
    val p = par.get
    val step = sz / p
    
    val ins = for (i <- 0 until p) yield new Looker(ct, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Looker(ct: ctries2.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      
      while (i < until) {
        ct.lookup(e(i))
        i += 1
      }
    }
  }
}


object MultiLookingCtrie2Snapshot extends Benchmark {
  import ctries2.ConcurrentTrie
  
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  for (i <- 0 until sz) ct.update(elems(i), elems(i))
  
  def run() {
    val p = par.get
    val step = sz / p
    val snap = ct.snapshot()
    
    val ins = for (i <- 0 until p) yield new Looker(snap, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Looker(snap: ctries2.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      
      while (i < until) {
        snap.lookup(e(i))
        i += 1
      }
    }
  }
}





