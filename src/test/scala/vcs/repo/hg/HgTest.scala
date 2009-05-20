package vcs.repo.hg

import java.util.UUID
import org.junit.Test
import org.junit.Assert._
import scalax.io.Implicits._
import scalax.data.Implicits._

class HgTest {

  def randomName = UUID.randomUUID.toString
  def randomDir = "/tmp/delete_me_456/" + randomName

  def writeRandomFile(repo:Repo) = {
    val fileName = repo.base + "/" + randomName + ".txt"
    for (writer <- fileName.toFile.writer; line <- 1 to 10) {
      writer.write(line + "\n")
    }
    fileName
  }

  def addAndCommitFile(repo:Repo) = {
    val file = writeRandomFile(repo)
    repo.add(file)
    repo.commit(randomName)
  }

  @Test
  def simpleTest() = {
    val changesets = 3
    val repo = HgManager.init(randomDir)
    for (i <- 1 to changesets) {
      addAndCommitFile(repo)
    }
    val log = repo.log
    assertEquals(changesets,log.size)
  }
}