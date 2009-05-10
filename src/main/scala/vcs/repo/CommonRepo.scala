package vcs.repo


import net.liftweb.util.IoHelpers

abstract class CommonRepo(val cmd:String,
                          val base:String) extends Repo {

  def add(path: String) = {
    IoHelpers.exec(cmd, "add", path)
  }

}