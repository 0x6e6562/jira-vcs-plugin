package vcs.repo.hg

object HgManager extends VCSManager {

  // TODO This needs to get parameterized some how
  val cmd = "/usr/local/bin/hg"


  def init(path: String) = {
    CommonManager.init(cmd,path)
    new HgRepo(path)
  }
}