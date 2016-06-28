import org.joda.time.DateTime
import org.mockito.Mockito.when
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class InterpreterSpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  private val Diego: User = User("Diego")
  private val Celine: User = User("Céline")
  private val Sandro: User = User("Sandro")

  private val Now = DateTime.now

  private val clock = mock[Clock]
  private val users = new Users
  private val messages = new Messages
  private val interpreter = new Interpreter(users, messages, clock)

  before {
    users.deleteAll()
    messages.deleteAll()
    when(clock.now).thenReturn(Now)
  }

  test("users should be created when they post a message for the first time") {
    interpreter.interpret("Diego -> hello world ")

    users.all() should contain (Diego)
  }

  test("users can post messages") {
    interpreter.interpret("Diego -> hello world ")

    messages.all() should contain (Message(Diego, "hello world", Now))
  }

  test("users can follow someone else") {
    users.add(Diego)
    users.add(Celine)
    users.add(Sandro)

    interpreter.interpret("Diego follows Céline")

    users.followedBy(Diego) should contain (Celine)
  }
}
