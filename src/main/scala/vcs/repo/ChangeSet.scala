package vcs.repo

import org.joda.time.DateTime

case class ChangeSet(val hash:String,
                     val branch:String,
                     val author:String,                     
                     val date:DateTime,
                     val desc:String,
                     var issueId:java.lang.Long) {

  def this(hash:String,
           branch:String,
           author:String,
           date:DateTime,
           desc:String) = this(hash,branch,author,date,desc,null)

}