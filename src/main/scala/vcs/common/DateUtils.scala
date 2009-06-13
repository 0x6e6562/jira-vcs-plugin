package vcs.common

import java.sql.{Timestamp, Date}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Provides the usual date calculation routines :-)
 */
object DateUtils {

  val formatter = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy Z");

  class CommonOperationsTimestamp(t:Timestamp) {
    def asDateTime = new DateTime(t.getTime)
  }

  class CommonOperationsJodaDate(d:DateTime) {
    def asTimeStamp = new Timestamp(d.getMillis)
  }

  class CommonOperationsString(s:String) {
    def asDateTime = formatter.parseDateTime(s)
  }

  implicit def sql2CommonOperationsSQLDate(t:Timestamp) = new CommonOperationsTimestamp(t)
  implicit def joda2CommonOperationsJodaDate(d:DateTime) = new CommonOperationsJodaDate(d)
  implicit def string2CommonOperationsString(s:String) = new CommonOperationsString(s)
}