package it.jira


import com.atlassian.jira.webtests.JIRAWebTest

/**
 * This uses Java inheretance and pre-Junit 4 method naming,
 * otherwise it won't compile :-(
 *
 */
class VcsJiraTest(val s:String) extends JIRAWebTest(s) {

  def testAdminister = {
    gotoPage("/secure/ListVcsRepositories.jspa");
    assertLinkPresentWithText("Add new repo");
  }

}