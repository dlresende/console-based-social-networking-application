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

  private var clock: Clock = _
  private var users: Users = _
  private var messages: Messages = _
  private var interpreter: Interpreter = _

  before {
    clock = mock[Clock]
    when(clock.now).thenReturn(Now)
    users = new Users
    messages = new Messages
    interpreter = new Interpreter(users, messages, clock)
  }

  test("users should be created when they post a message for the first time") {
    interpreter.handle("Diego -> hello world ")

    users.all() should contain (Diego)
  }

  test("users can post messages") {
    interpreter.handle("Diego -> hello world ")

    messages.all() should contain (Message(Diego, "hello world", Now))
  }

  test("users can follow someone else") {
    users.add(Diego)
    users.add(Celine)
    users.add(Sandro)

    interpreter.handle("Diego follows Céline")

    users.followedBy(Diego) should contain (Celine)
  }

  test("an exception should be thrown when a user that doesn't exist tries to follow another user") {
    evaluating { interpreter.handle("NonExistingUser follows Diego") } should produce [RuntimeException]
  }

  test("an exception should be thrown when a user tries to follow another user that doesn't exist") {
    users.add(Diego)

    evaluating { interpreter.handle("Diego follows NonExistingUser") } should produce [RuntimeException]
  }

  test("an exception should be thrown when someone tries to display the wall of a user that doesn't exist") {
    evaluating { interpreter.handle("NonExistingUser wall") } should produce [RuntimeException]
  }
}
