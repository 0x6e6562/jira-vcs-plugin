package vcs.linking


import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.tabpanels.GenericMessageAction
import com.opensymphony.user.User
import org.scala_tools.javautils.Imports._
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel

class ChangeSetTabPanel extends AbstractIssueTabPanel {


  def showPanel(issue:Issue,remoteUser:User) = true

  def getActions(issue:Issue,remoteUser:User) = {
    val changesets = EntityManager.changeSetsByIssue(issue)
    changesets.size match {
      case 0 => {
        List(new GenericMessageAction(getText("no.log.entries.message"))).asJava
      }

    }
  }

  def getText(key:String) = descriptor.getI18nBean().getText(key)

}