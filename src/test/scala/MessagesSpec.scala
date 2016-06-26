import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class MessagesSpec extends FunSuite with ShouldMatchers {

  val Diego = User("Diego")
  val Celine = User("Céline")

  test("that messaged can be added") {
    val messages = new Messages()
    messages.add(Message(Diego, "hello"))
    messages.add(Message(Celine, "Hi"))

    messages.all() should be (Set(Message(Diego, "hello"), Message(Celine, "Hi")))
  }
}
