import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class MessagesSpec extends FunSuite with ShouldMatchers {

  val Diego = User("Diego")
  val Celine = User("Céline")

  test("messages can be added") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello"))
    messages.add(Message(Celine, "Hi"))

    messages.all() should be (Set(Message(Diego, "hello"), Message(Celine, "Hi")))
  }

  test("messages can be found dy user") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello"))
    messages.add(Message(Diego, "world"))
    messages.add(Message(Celine, "Hi"))

    messages.findBy(Diego) should be (Set(Message(Diego, "hello"), Message(Diego, "world")))
  }
}
