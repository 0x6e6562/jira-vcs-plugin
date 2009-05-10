package vcs.repo


import net.liftweb.util.IoHelpers

trait VCSManager {

  def init(path:String) : Repo
  
}


object CommonManager {

  def init(cmd:String, path:String) : Unit = {
    IoHelpers.exec(cmd, "init", path)
  }
}





