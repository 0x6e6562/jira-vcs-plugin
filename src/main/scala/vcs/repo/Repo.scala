package vcs.repo

/**
 * This defines the operations you can perform on a Repo
 */
trait Repo {

  /**
   * Create a new repository in the given location
   */
  def add(path:String) : Unit

}