package vcs.repo.hg

object Stopwatch {

  def timer[T](x:Function0[T]) : (Long,T) = {
    val begin = System.currentTimeMillis
    val result = x()
    val end = System.currentTimeMillis
    val time = (end - begin)
    (time,result)
  }

  def printTime[T](x:Function0[T],label:String) : T = {
    val (time,result) = timer(x)
    println(label + " took " + time + " ms")
    result
  }

}