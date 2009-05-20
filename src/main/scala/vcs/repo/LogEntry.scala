package vcs.repo

case class LogEntry(val hash:String,
                    val author:String,
                    val date:String,
                    val summary:String)