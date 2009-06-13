package vcs.linking


import com.atlassian.jira.ComponentManager
import com.atlassian.jira.service.AbstractService
import org.apache.log4j.Logger

/**
 * I don't understand why you can't constructor inject the ChangeSetLinker
 */
class TrawlingService extends AbstractService {

  // TODO This kind of thing should be injected, not looked up
  val changeSetLinker = ComponentManager.getInstance.getContainer.getComponentInstanceOfType(classOf[ChangeSetLinker]).asInstanceOf[ChangeSetLinker]

  def getObjectConfiguration = {
    getObjectConfiguration("VCSTRAWLINGSERVICE", "services/plugins/vcs/changeset-trawling.xml", null);
  }

  def run = {
    log.info("Running the VCS trawling service ...... ")
    println("Actually runnning *8888 &&&*&SAD*^SD*&^AS*&^*&S^D*&SD^*&^DS*&S^DA*&S^D*&^SD*&^")
    changeSetLinker.trawl
  }

}