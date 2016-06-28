import org.joda.time.DateTime._
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Display() {

  def print(messages: Iterable[Message]): Unit = print(messages.toArray:_*)

  def print(messages: Message*) = {
    messages.foreach(message => doPrint(message))
  }

  private def doPrint(message: Message): Unit = {
    val period = new Period(message.postTime, now)

    val formatter = new PeriodFormatterBuilder()
      .appendPrefix(" (")
      .appendMinutes().appendSuffix(" minutes ago")
      .appendSuffix(")")
      .printZeroNever()
      .toFormatter

    println(message.content + formatter.print(period))
  }
}
