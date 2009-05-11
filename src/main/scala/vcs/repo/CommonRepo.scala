package vcs.repo


import net.liftweb.util.IoHelpers

abstract class CommonRepo(val cmd:String,
                          val base:String) extends Repo {

  def add(path: String) = {
    IoHelpers.exec(cmd, "add", path)
  }


  def commit(message: String) = {
    IoHelpers.exec(cmd, "commit", "-R", base, "-m", message)    
  }


  def log = {
    val output = IoHelpers.exec(cmd, "log", "-R", base)
    // TODO There must be a cleaner way to get the output
    output.productElement(0).asInstanceOf[String]
  }
}