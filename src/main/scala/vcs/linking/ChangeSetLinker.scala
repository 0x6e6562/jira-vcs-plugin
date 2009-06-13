package vcs.linking

import com.atlassian.jira.ComponentManager
import configuration.RepositoryList
import repo.ChangeSet

class ChangeSetLinker {

  // TODO This kind of thing should be injected, not looked up
  val repositoryList = ComponentManager.getInstance.getContainer.getComponentInstanceOfType(classOf[RepositoryList]).asInstanceOf[RepositoryList]
  val issueManager = ComponentManager.getInstance.getIssueManager

  def maybeLinkChangeset(changeset:ChangeSet) = {
    if (!EntityManager.isIndexed(changeset)) {

      if (changeset.branch != null) {
        val issue = issueManager.getIssueObject(changeset.branch)
        if (null != issue) {
          changeset.issueId = issue.getId
        }
      }

      EntityManager.add(changeset)
    }
  }

  def trawl = {
    repositoryList.listRepositories.foreach(
      repository => {
        println("Repos base: " + repository.base)
        repository.log().reverse.foreach(maybeLinkChangeset(_))
      }
    )
  }

}