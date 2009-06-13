package vcs.linking


import com.atlassian.jira.ComponentManager
import com.atlassian.jira.issue.Issue
import java.sql.Timestamp
import java.util.Date
import org.joda.time.DateTime
import org.ofbiz.core.entity.{GenericValue, DelegatorInterface}
import repo.ChangeSet
import org.scala_tools.javautils.Imports._
import common.DateUtils._

/**
 * Does a lot of boilerplate that *should* actually be handled by the ORM framework,
 * but seemingly, isn't
 */
object EntityManager {

  val CHANGESET = "ChangeSet"
  val HASH = "hash"
  val AUTHOR = "author"
  val BRANCH = "branch"
  val CREATED = "created"
  val FIRSTLINE = "firstline"
  val SEQUENCE = "sequence"
  val ISSUE = "issue"

  val ofBizDelegator:DelegatorInterface =
    ComponentManager.getComponentInstanceOfType(classOf[DelegatorInterface]).asInstanceOf[DelegatorInterface];

  def changeSetsByIssue(issue:Issue) = {
    val changeSets = ofBizDelegator.findByAnd(CHANGESET, Map(ISSUE -> issue.getId).asJava).asInstanceOf[java.util.List[GenericValue]]
    changeSets.asScala.map(toChangeSet)
  }

  def nextChangeSetSequence = ofBizDelegator.getNextSeqId(CHANGESET)

  def add(changeSet:ChangeSet) = ofBizDelegator.create(CHANGESET,toJavaMap(changeSet))

  def isIndexed(changeSet:ChangeSet) : Boolean = {
    findByHash(changeSet.hash) match {
      case null => false
      case _    => true
    }
  }

  def findByHash(hash:String) : ChangeSet = {
    ofBizDelegator.findByPrimaryKey(CHANGESET,Map(HASH -> hash).asJava) match {
      case null => null
      case x    => toChangeSet(x)
    }
  }

  def toJavaMap(changeSet:ChangeSet) = {
    Map(HASH -> changeSet.hash,
        AUTHOR -> changeSet.author,
        BRANCH -> changeSet.branch,
        CREATED ->  changeSet.date.asTimeStamp,
        SEQUENCE -> nextChangeSetSequence,
        ISSUE -> changeSet.issueId,
        FIRSTLINE -> changeSet.desc).asJava
  }

  def toChangeSet(genericValue:GenericValue) = {
    new ChangeSet(
          genericValue.getString(HASH),
          genericValue.getString(BRANCH),
          genericValue.getString(AUTHOR),
          genericValue.getTimestamp(CREATED).asDateTime,
          genericValue.getString(FIRSTLINE),
          genericValue.getLong(ISSUE)
        )
  }
}