package vcs.repo.hg

import collection.mutable.{ListBuffer, Buffer}
import java.io.File
import java.util.UUID
import java.util.Random
import org.junit.Assert._
import org.junit.{After, Before, Test}
import scalax.io.Implicits._
import scalax.data.Implicits._
import Stopwatch._
import org.scalacheck._
import vcs.common.QAUtils._

class HgTest {

  /**
   * Very crude random vocabulary that only uses ASCII chars
   */
  val vocabulary = {
    val chars = List.range(1,127)
    val symbols = Gen.elements(chars.map(x=>(x+'\0').toChar.toString) : _*)
    val numberOfWords = 100
    var words = new ListBuffer[String]()

    def buildWord = {
      val rand = new Random()
      val next = rand.nextInt(10) + 1
      var word = ""
      for (y <- 1 to next) {
        word += symbols.sample.get
      }
      word
    }

    for (x <- 1 to numberOfWords) {
      words += buildWord
    }

    Gen.elements(words : _*)
  }

  val testDir = new File("target/unit_test_dirs/" + randomName)

  @Before
  def createTestDir : Unit = testDir.mkdirs

  @After
  def deleteTestDir : Unit = testDir.deleteRecursively

  def writeRandomFile(repo:Repo) = {
    val fileName = repo.base + "/" + randomName + ".txt"
    for (writer <- fileName.toFile.writer; line <- 1 to 10) {
      writer.write(randomLine(10) + "\n")
    }
    fileName
  }

  def randomLine(words:Int) = {
    var line = new ListBuffer[String]()
    var rand = new Random()
    for (x <- 1 to words) {
      line += vocabulary.sample.get
    }
    line.mkString(" ")
   }


  def addAndCommitFile(repo:Repo) = {
    val file = writeRandomFile(repo)
    repo.add(file)
    repo.commit(randomLine(20))
  }

  def logLastAndPartial(repo:Repo,fullLog:List[ChangeSet]) = {
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
  def branchTest = {
    val branch = randomName
    val repo = HgManager.init(testDir.getPath)
    addAndCommitFile(repo)
    repo.branch(branch)
    addAndCommitFile(repo)
    val changesets = repo.log()
    assertEquals(2,changesets.size)
    assertEquals(changesets(0).branch,branch)    
  }

  @Test
  def simpleTest() = {
    val changesets = 2

    val repo = HgManager.init(testDir.getPath)
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
    println(fullLog.size)
    logLastAndPartial(repo,fullLog)    
  }
}