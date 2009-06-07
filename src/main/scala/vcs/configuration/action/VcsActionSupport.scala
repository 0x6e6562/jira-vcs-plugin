package vcs.configuration.action


import com.atlassian.jira.web.action.JiraWebActionSupport

import reflect.BeanProperty
import repo.hg.HgRepo
import org.scala_tools.javautils.Imports._

class VcsActionSupport(val repositoryList:RepositoryList) extends JiraWebActionSupport {

  /**
   * Expose this as a Java collection to play well with Velocity
   */
  def listRepositories = repositoryList.listRepositories.asJava

  def redirect = getRedirect("ListVcsRepositories.jspa")
}

class VcsAddActionSupport(override val repositoryList:RepositoryList)
        extends VcsActionSupport(repositoryList) {

  @BeanProperty var path = "/tmp"

  override def doExecute = {
    val repo = new HgRepo(path)
    repositoryList.add(repo)
    redirect
  }
}

class VcsDeleteActionSupport(override val repositoryList:RepositoryList)
        extends VcsActionSupport(repositoryList) {

  @BeanProperty var repoId = "-1"

  override def doExecute = {
    repositoryList.delete(repoId.toLong)
    redirect
  }
}