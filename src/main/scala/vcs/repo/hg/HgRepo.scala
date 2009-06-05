package vcs.repo.hg



import net.liftweb.util.{Box, IoHelpers}
import util.parsing.combinator.JavaTokenParsers

class HgRepo(override val cmd:String,
             override val base:String) extends CommonRepo(cmd, base) {

  def this(base:String) = this(HgManager.cmd,base)

  def template = """{node}\n{author|user}\n{date|shortdate}\n{desc\firstline}\n"""

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

    def parse(s:String) : List[LogEntry] = parseAll(entries, s) match {
      case Success(x,s) => x
      // TODO There must be a way to do this on a token by token basis
      case failure => throw new RuntimeException("Could not parse log entries: " + s)
    }

    def entries : Parser[List[LogEntry]] = rep(entry)

    def entry : Parser[LogEntry] = hash~author~date~desc ^^
      {case hash~author~date~desc => LogEntry(hash,author,date,desc)}

    def author = """(.)+""".r
    def desc   = """(.)+""".r
    def hash   = """[a-z0-9]{40,40}""".r
    def date   = """[0-9]{4,4}-[0-9]{2,2}-[0-9]{2,2}""".r
  }
}