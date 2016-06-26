import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends FunSuite with ShouldMatchers with BeforeAndAfter {

  val Diego: User = User("Diego")

  val users = new Users
  val messages: Messages = new Messages()
  var interpreter = new Interpreter(users, messages)

  before {
    users.deleteAll()
  }

  test("that users should be created when they post a message for the first time") {
    interpreter.interpret("posting: Diego -> hello world ")

    users.all() should contain (Diego)
  }

  test("that users can post messages") {
    users.add(Diego)

    interpreter.interpret("posting: Diego -> hello world ")

    messages.all() should contain (Message(Diego, "hello world"))
  }
}
