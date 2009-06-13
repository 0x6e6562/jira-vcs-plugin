package vcs.repo.hg

import net.liftweb.util.{Box, IoHelpers}
import util.parsing.combinator.JavaTokenParsers
import common.DateUtils._

class HgRepo(override val cmd:String,
             override val base:String) extends CommonRepo(cmd, base) {

  def this(base:String) = this(HgManager.cmd,base)

  def template = """{node}:{branches}\n{author|user}\n{date|date}\n{desc|firstline}\n"""

  override def toString = base

  override def log(changesets:String*) = {
    val output = changesets.length match {
      case 0 => IoHelpers.exec(cmd, "log", "--template", template, "-R", base)
      case 1 => IoHelpers.exec(cmd, "log", "--template", template, "-R", base, "-r", changesets(0) + ":")
      case 2 => IoHelpers.exec(cmd, "log", "--template", template, "-R", base, "-r", changesets(0) + ":" + changesets(1))
    }
    // TODO There must be a cleaner way to get the output

    HgLogParser.parse(output.productElement(0).asInstanceOf[String])
  }

  object HgLogParser extends JavaTokenParsers {

    def parse(s:String) : List[ChangeSet] = parseAll(entries, s) match {
      case Success(x,s) => x
      // TODO There must be a way to do this on a token by token basis
      case failure => throw new RuntimeException("Could not parse log entries: " + s)
    }

    def entries : Parser[List[ChangeSet]] = rep(entry)

    def entry : Parser[ChangeSet] = {
      hashbranch~author~date~desc ^^
        {
          case hashbranch~author~date~desc => {
            val parts = hashbranch.split(":")
            val (hash,branch) = parts.length match {
              case 1 => (parts(0),null)
              case 2 => (parts(0),parts(1))
            }
            new ChangeSet(hash,branch,author,date.asDateTime,desc)
          }
        }
    }

    def author     = """(.)+""".r
    def desc       = """(.)+""".r
    def branch     = """(.)+""".r
    def hashbranch = """[a-z0-9]{40,40}:(.)*""".r
    def date       = """(.)+""".r // TODO Think about a regex for this
  }
}