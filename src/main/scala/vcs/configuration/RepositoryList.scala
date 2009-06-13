package vcs.configuration

import collection.jcl.{SortedMap, TreeMap}
import com.atlassian.jira.config.properties.ApplicationProperties
import com.atlassian.jira.issue.changehistory.ChangeHistoryManager
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.project.version.VersionManager
import com.atlassian.jira.propertyset.JiraPropertySetFactory
import com.atlassian.jira.security.PermissionManager
import com.atlassian.jira.service.ServiceManager
import com.opensymphony.module.propertyset.PropertySet
import linking.{TrawlingService, ChangeSetLinker}
import org.apache.log4j.Logger
import repo.hg.HgRepo
import repo.Repo

trait RepositoryList {

  def listRepositories : Seq[Repo]

  def add(repository:Repo) : Repo

  def update(repository:Repo) : Unit

  def delete(id:Long) : Unit
  
}

/**
 * These are the keys used to store the
 */
object RepositoryAttributes {
  val ID   = "plugins.vcs.id"
  val BASE = "plugins.vcs.base"
}

class RepositoriesManager(val jiraPropertySetFactory:JiraPropertySetFactory) extends RepositoryList {

  val log = Logger.getLogger(getClass)

  var repositories: SortedMap[Long, PropertySet] = new TreeMap()

  val REPOSITORY_LIST = "jira.plugins.vcs.repository.keys"
  val REPO_PROPERTY = "jira.plugins.vcs.repo";

  val repositoryKeys = jiraPropertySetFactory.buildCachingDefaultPropertySet(REPOSITORY_LIST,true)
  val keys = List.fromArray(repositoryKeys.getKeys.toArray).map(_.asInstanceOf[String].toLong)
  
  keys.foreach(key => {
      val propertySet = jiraPropertySetFactory.buildCachingPropertySet(REPO_PROPERTY,key,true)
      repositories.put(key,propertySet)
    })

  def listRepositories = repositories.values.map(x => read(x)).toList

  def add(repository:Repo) = {
    val key = repositories.size match {
      case 0 => 1L
      case _ => repositories.lastKey + 1L
    }
    repository.id = key
    val propertySet = jiraPropertySetFactory.buildCachingPropertySet(REPO_PROPERTY,key,true)
    write(propertySet,repository)
    repository
  }

  // TODO This is not wired up yet
  def update(repository:Repo) = write(repositories(repository.id),repository)

  def delete(key:Long) = {
    val propertySet = repositories(key)
    repositoryKeys.remove(key.toString)
    repositories.removeKey(key)
    // TODO Need to clarify how to remove this from persistence - see forum post
    // propertySet.remove()
    // TODO What about deleting the stored metadata associated with this repository ?
  }

  private def read(propertySet:PropertySet) : Repo = {
    val repository = new HgRepo(propertySet.getString(RepositoryAttributes.BASE))
    repository.id = propertySet.getLong(RepositoryAttributes.ID)
    repository
  }

  private def write(propertySet:PropertySet,repository:Repo) : Unit = {
    val key = repository.id
    propertySet.setLong(RepositoryAttributes.ID,repository.id)
    propertySet.setString(RepositoryAttributes.BASE,repository.base)
    repositoryKeys.setLong(key.toString,key)
    repositories.put(key,propertySet)
  }
}