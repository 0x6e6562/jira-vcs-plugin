package vcs.repo.hg

class HgRepo(override val cmd:String,
             override val base:String) extends CommonRepo(cmd, base) {

  def this(base:String) = this(HgManager.cmd,base)


  override def toString = base
}