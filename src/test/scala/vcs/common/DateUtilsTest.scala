package vcs.common


import org.joda.time.{DateTimeZone, DateTime}
import org.junit.Test
import vcs.common.DateUtils._
import org.junit.Assert._

class DateUtilsTest {

  @Test
  def parseDate = {
    val date = new DateTime(2009,06,10,15,20,56,0)
    val parsed = "Wed Jun 10 15:20:56 2009 +0100".asDateTime
    assertEquals(date,parsed)
  }
}