package vcs.repo.hg

import java.util.UUID
import org.junit.Test
import org.junit.Assert._
import scalax.io.Implicits._
import scalax.data.Implicits._
import Stopwatch._

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

  def logLastAndPartial(repo:Repo,fullLog:List[LogEntry]) = {
    val changesets = fullLog.size
    val index1 = changesets / 2
    val hash1 = fullLog(index1).hash

    val sinceLog = printTime(() => repo.log(hash1), "Since log")

    assertEquals(changesets - index1 + 1, sinceLog.size)

    val index2 = (changesets - index1) / 2
    val hash2 = fullLog(index2).hash
    val partialLog = printTime(() => repo.log(hash1,hash2), "Partial log")

    assertEquals(index1 - index2 + 1, partialLog.size)
  }

  @Test
  def simpleTest() = {
    val changesets = 20

    val repo = HgManager.init(randomDir)
    for (i <- 1 to changesets) {
      addAndCommitFile(repo)
    }

    val fullLog = printTime(() => repo.log(), "Full log")
    assertEquals(changesets,fullLog.size)
    logLastAndPartial(repo,fullLog)
  }


}

object Benchmark extends HgTest {
  def main(args: Array[String]) {
    val repo = new HgRepo(args(0))
    val fullLog = printTime(() => repo.log(), "Full log")
    println(fullLog)
    logLastAndPartial(repo,fullLog)    
  }
}