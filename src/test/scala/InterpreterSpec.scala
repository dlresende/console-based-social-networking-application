import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends FunSuite with ShouldMatchers with BeforeAndAfter {

  val Diego: User = User("Diego")
  val Celine: User = User("Céline")
  val Sandro: User = User("Sandro")

  val users = new Users
  val messages: Messages = new Messages()
  var interpreter = new Interpreter(users, messages)

  before {
    users.deleteAll()
    messages.deleteAll()
  }

  test("users should be created when they post a message for the first time") {
    interpreter.interpret("Diego -> hello world ")

    users.all() should contain (Diego)
  }

  test("users can post messages") {
    users.add(Diego)

    interpreter.interpret("Diego -> hello world ")

    messages.all() should contain (Message(Diego, "hello world"))
  }

  test("users can follow someone else") {
    users.add(Diego)
    users.add(Celine)
    users.add(Sandro)

    interpreter.interpret("Diego follows Céline")

    users.followers(Diego) should contain (Celine)
  }
}
