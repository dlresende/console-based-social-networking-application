import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Display(clock: Clock) {

  def print(messages: Iterable[Message]): Unit = print(messages.toArray:_*)

  def print(messages: Message*) = {
    messages.foreach(message => doPrint(message))
  }

  private def doPrint(message: Message): Unit = {
    println(toString(message))
  }

  def toString(message: Message) = {
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

    message.content + " " + formatter.print(elapsedTime)
  }

}
