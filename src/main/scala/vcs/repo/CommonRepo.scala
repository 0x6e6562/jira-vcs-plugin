package vcs.repo


import java.io.File
import net.liftweb.util.IoHelpers

abstract class CommonRepo(val cmd:String,
                          val base:String) extends Repo {

  def add(path:File) = add(path.getPath)

  def add(path: String) = {
    IoHelpers.exec(cmd, "add", path)
  }

  def branch(name:String) = IoHelpers.exec(cmd,"branch",name,"-R",base)

  def commit(message: String) = {
    IoHelpers.exec(cmd, "commit", "-R", base, "-m", message)    
  }


//  def log = {
//    val output = IoHelpers.exec(cmd, "log", "-R", base)
//    // TODO There must be a cleaner way to get the output
//    output.productElement(0).asInstanceOf[String]
//  }
}