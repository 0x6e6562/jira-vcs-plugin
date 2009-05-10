package vcs.repo.hg


import java.util.UUID
import org.junit.Test

class HgTest {

  def randomDir = "/tmp/" + UUID.randomUUID.toString

  @Test
  def initTest() = {
    val hgRepo = HgManager.init(randomDir)
    println(hgRepo)
  }
}