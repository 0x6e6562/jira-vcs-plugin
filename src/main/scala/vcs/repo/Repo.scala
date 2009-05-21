package vcs.repo

/**
 * This defines the operations you can perform on a Repo
 */
trait Repo {

  /**
   * The base directory of the repository
   */
  val base:String

  /**
   * Create a new repository in the given location
   */
  def add(path:String) : Unit

  /**
   * Commits the current working directory to the index
   */
  def commit(message:String) : Unit

  /**
   * Reads the log from the repo
   */
  def log(changesets:String*) : List[LogEntry]

}