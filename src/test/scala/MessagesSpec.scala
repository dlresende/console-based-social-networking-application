import org.joda.time.DateTime
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class MessagesSpec extends FunSuite with ShouldMatchers {

  private val Diego = User("Diego")
  private val Celine = User("Céline")

  private val Now = DateTime.now

  test("messages can be added") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello", Now))
    messages.add(Message(Celine, "Hi", Now))

    messages.all() should be (Set(Message(Diego, "hello", Now), Message(Celine, "Hi", Now)))
  }

  test("messages can be found dy user") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello", Now))
    messages.add(Message(Diego, "world", Now))
    messages.add(Message(Celine, "Hi", Now))

    messages.findBy(Diego) should be (Set(Message(Diego, "hello", Now), Message(Diego, "world", Now)))
  }
}
