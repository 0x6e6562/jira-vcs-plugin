package vcs.repo


import reflect.BeanProperty

/**
 * This defines the operations you can perform on a Repo
 */
trait Repo {

  /**
   * Identifies the repo as a persistent property set
   */
  // TODO This seems to mix up the pure VCS layer from the JIRA layer
  // TODO It is probably also bad practice to make this mutable :-(
  @BeanProperty var id:Long = -1

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