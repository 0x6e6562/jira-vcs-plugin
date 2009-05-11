package vcs.repo.hg



import java.util.UUID
import org.junit.Test
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

  @Test
  def simpleTest() = {
    val repo = HgManager.init(randomDir)
    val file = writeRandomFile(repo)
    repo.add(file)
    repo.commit(randomName)
    val log = repo.log

    println("Wrote :" + file)
    println("Log :" + log)

  }
}