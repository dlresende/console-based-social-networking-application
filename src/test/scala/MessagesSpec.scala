import org.joda.time.DateTime.now
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.mutable

class MessagesSpec extends FunSuite with ShouldMatchers with BeforeAndAfter {

  private val Diego = User("Diego")
  private val Celine = User("Céline")

  private val Now = now

  private var messages:Messages = _

  before {
    messages = new Messages()
  }

  test("messages can be added") {
    messages add Message(Diego, "hello", Now)
    messages add Message(Celine, "Hi", Now)

    messages.findBy(Diego) should contain (Message(Diego, "hello", Now))
    messages.findBy(Celine) should contain (Message(Celine, "Hi", Now))
  }

  test("messages can be found by user") {
    messages add Message(Diego, "hello", Now)
    messages add Message(Diego, "world", Now)
    messages add Message(Celine, "Hi", Now)

    val messagesFromDiego = messages.findBy(Diego)

    messagesFromDiego should be (mutable.Stack(Message(Diego, "world", Now), Message(Diego, "hello", Now)))
  }

  test("most recent messages should be displayed first") {
    messages add Message(Diego, "1", Now plusMinutes 1)
    messages add Message(Diego, "2", Now plusMinutes 2)
    messages add Message(Celine, "3", Now plusMinutes 3)
    messages add Message(Celine, "4", Now plusMinutes 4)
    messages add Message(Diego, "5", Now plusMinutes 5)

    val allMessages = messages.findBy(Diego, Celine)

    allMessages should be (mutable.Stack(
      Message(Diego, "5", Now plusMinutes 5 ),
      Message(Celine, "4", Now plusMinutes 4),
      Message(Celine, "3", Now plusMinutes 3),
      Message(Diego, "2", Now plusMinutes 2),
      Message(Diego, "1", Now plusMinutes 1)))
  }
}
