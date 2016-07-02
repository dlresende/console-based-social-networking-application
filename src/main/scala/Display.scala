import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Display(clock: Clock) {

  def timeline(messages: Iterable[Message]): Unit = {
    messages.foreach(message => doPrint(message, shouldPrintAuthor = false))
  }

  def wall(messages: Iterable[Message]) = {
    messages.foreach(message => doPrint(message, true))
  }

  private def doPrint(message: Message, shouldPrintAuthor:Boolean): Unit = {
    println(toString(message, shouldPrintAuthor))
  }

  def toString(message: Message, shouldPrintAuthor: Boolean = false) = {
    val elapsedTime = new Period(message.postTime, clock.now)

    val formatter = new PeriodFormatterBuilder()
      .appendPrefix("(")
      .appendSeconds().appendSuffix(" second ago", " seconds ago")
      .appendSuffix(")")
      .appendPrefix("(")
      .appendMinutes().appendSuffix(" minute ago", " minutes ago")
      .appendSuffix(")")
      .printZeroNever()
      .toFormatter

    val contentAndElapsedTime = message.content + " " + formatter.print(elapsedTime)

    if(shouldPrintAuthor)
      message.author.name + " - " + contentAndElapsedTime
    else
      contentAndElapsedTime
  }

}
