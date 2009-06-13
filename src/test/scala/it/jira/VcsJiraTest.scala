package it.jira

import com.atlassian.jira.webtests.JIRAWebTest
import java.io.File
import org.junit.Assert._
import vcs.common.QAUtils._
import org.springframework.core.io.ClassPathResource
import scalax.io.Implicits._
import vcs.linking.ChangeSetLinker
import vcs.repo.hg.{HgManager, HgRepo}

/**
 * This uses Java inheretance and pre-Junit 4 method naming,
 * otherwise it won't compile :-(
 *
 */
class VcsJiraTest(val s:String) extends JIRAWebTest(s) {

  val PROJECT_KEY = "ABC"

  val testRepoDir = new File(getProperty("test.repository.dir") + "/" + randomName)

  def getProperty(k:String) = getEnvironmentData.getProperty(k)

//  def testAdminister = {
//    gotoPage("/secure/ListVcsRepositories.jspa");
//    assertLinkPresentWithText("Add new repo");
//  }

  override def setUp = {
    super.setUp
    deleteAllProjects
    testRepoDir.mkdirs
  }


  override def tearDown = {
    super.tearDown
    testRepoDir.deleteRecursively
  }

  def testLowlevel = {
    createTestProject
    val issue = createTestIssue

    val repository = HgManager.init(testRepoDir.getPath)

    repository.add(copy("testfiles/first.txt", "foo.txt"))
    repository.commit("The initial check in")
    repository.branch(issue)
    repository.add(copy("testfiles/first.txt", "bar.txt"))
    repository.commit("Added a second file")

    gotoPage("/secure/ListVcsRepositories.jspa");
    clickLinkWithText("Add new repo")
    setFormElement("path", testRepoDir.getPath)
    submit()


    waitForTrawl



    gotoPage("/secure/ListVcsRepositories.jspa");
    submit("delete")

    // Fuck knows why Junit doesn't think this is a test unless you
    // assert something - I guess you have to be assertive :-)
    assertTrue(true)
  }

  def copy(sourceName:String,destName:String) : File = {
    val source = new ClassPathResource(sourceName)
    val dest = new File(testRepoDir,destName)
    source.getFile.copyTo(dest)
    dest
  }

  def createTestIssue = addIssue("Test Project", PROJECT_KEY, JIRAWebTest.ISSUE_TYPE_BUG, "Summary Of Bug 1",
                                  null,null,null,null,null,null,null,null,null,null);

  def createTestProject = addProject("Test Project",PROJECT_KEY,null,"admin",null)


  def waitForTrawl = {
    println("Waiting for re-trawl.......")
    Thread.sleep(65000)
  }
}