import org.joda.time.DateTime.now
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable

class MessagesSpec extends FunSuite with ShouldMatchers {

  private val Diego = User("Diego")
  private val Celine = User("Céline")

  private val Now = now

  test("messages can be added") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello", Now))
    messages.add(Message(Celine, "Hi", Now))

    val allMessages = messages.all()

    allMessages should be (Set(Message(Diego, "hello", Now), Message(Celine, "Hi", Now)))
  }

  test("messages can be found dy user") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello", Now))
    messages.add(Message(Diego, "world", Now))
    messages.add(Message(Celine, "Hi", Now))

    val messagesFromDiego = messages.findBy(Diego)

    messagesFromDiego should be (Set(Message(Diego, "hello", Now), Message(Diego, "world", Now)))
  }

  test("messages should be sorted by ascending creation time") {
    val messages = new Messages()
    messages.add(Message(Diego, "1", Now.plusMinutes(1)))
    messages.add(Message(Diego, "2", Now.plusMinutes(2)))
    messages.add(Message(Celine, "3", Now.plusMinutes(3)))
    messages.add(Message(Celine, "4", Now.plusMinutes(4)))
    messages.add(Message(Diego, "5", Now.plusMinutes(5)))

    val allMessages = messages.findBy(Diego, Celine)

    allMessages should be (mutable.LinkedHashSet[Message](
      Message(Diego, "1", Now.plusMinutes(1)),
      Message(Diego, "2", Now.plusMinutes(2)),
      Message(Celine, "3", Now.plusMinutes(3)),
      Message(Celine, "4", Now.plusMinutes(4)),
      Message(Diego, "5", Now.plusMinutes(5))))
  }
}
