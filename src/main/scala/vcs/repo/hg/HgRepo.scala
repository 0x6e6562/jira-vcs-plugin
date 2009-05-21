package vcs.repo.hg



import net.liftweb.util.{Box, IoHelpers}
import util.parsing.combinator.JavaTokenParsers

class HgRepo(override val cmd:String,
             override val base:String) extends CommonRepo(cmd, base) {

  def this(base:String) = this(HgManager.cmd,base)

  def template = """|{node}|{author|user}|{date|shortdate}|{desc}|"""

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
      case failure => List()
    }

    def entries : Parser[List[LogEntry]] = rep(entry)

    def entry : Parser[LogEntry] = "|"~hash~"|"~author~"|"~date~"|"~desc~"|" ^^
      {case x~hash~y~author~z~date~p~desc~u => LogEntry(hash,author,date,desc)}

    def author = """[a-z0-9]+""".r
    def hash   = """[a-z0-9]+""".r
    def date   = """[0-9]{4,4}-[0-9]{2,2}-[0-9]{2,2}""".r
    def desc   = """[a-z0-9-]+""".r
  }
}