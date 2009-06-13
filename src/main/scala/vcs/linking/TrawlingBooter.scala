package vcs.linking

import com.atlassian.jira.service.ServiceManager
import org.apache.log4j.Logger

/**
 * This is designed to let Pico boot up whatever it needs
 * to boot up and start the trawling service
 *
 * WTF is this kind of shit not declarable in the plugin config ????
 *
 * Who designed this API????
 *
 * Or is it just some undocumented feature ????
 *
 * Or is that Pico jut doesn't want any XML config?
 */
class TrawlingBooter(val serviceManager:ServiceManager) {

  val log = Logger.getLogger(getClass)

  val serviceName = "jira.plugins.vcs.changeset-indexer"

  if (null == serviceManager.getServiceWithName(serviceName)) {
    log.info("Configuring the trawling service")
    serviceManager.addService(serviceName,classOf[TrawlingService].getName,60*1000)
  }
  
}