import org.joda.time.DateTime
import org.mockito.Mockito.{verify, when}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.Option.empty
import scala.collection.{immutable, mutable}

class InterpreterSpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  private val Diego = User("Diego")
  private val Celine = User("Céline")
  private val Sandro = User("Sandro")

  private val Now = DateTime.now

  private var clock: Clock = _
  private var display: Display = _
  private var users: Users = _
  private var messages: Messages = _
  private var interpreter: Interpreter = _

  before {
    clock = mock[Clock]
    when(clock.now).thenReturn(Now)
    display = mock[Display]
    users = mock[Users]
    messages = mock[Messages]
    interpreter = new Interpreter(users, messages, clock, display)
  }

  test("users should be created when they post a message for the first time") {
    when(users.findByName("Diego")).thenReturn(empty)

    interpreter.handle("Diego -> hello world ")

    verify(users).add(Diego)
  }

  test("users can post messages") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))

    interpreter.handle("Diego -> hello world ")

    verify(messages).add(Message(Diego, "hello world", Now))
  }

  test("users can follow someone else") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(users.findByName("Céline")).thenReturn(Option(Celine))

    interpreter.handle("Diego follows Céline")

    verify(users).addFollower(Diego, Celine)
  }

  test("an exception should be thrown when a user that doesn't exist tries to follow another user") {
    evaluating { interpreter.handle("NonExistingUser follows Diego") } should produce [RuntimeException]
  }

  test("an exception should be thrown when a user tries to follow another user that doesn't exist") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))

    evaluating { interpreter.handle("Diego follows NonExistingUser") } should produce [RuntimeException]
  }

  test("an exception should be thrown when someone tries to display the wall of a user that doesn't exist") {
    evaluating { interpreter.handle("NonExistingUser wall") } should produce [RuntimeException]
  }
  
  test("user's wall should display his messages and messages from people he follows") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(users.followedBy(Diego)).thenReturn(Set(Celine))
    when(messages.findBy(Set(Diego, Celine))).thenReturn(Set(Message(Diego, "hello", Now), Message(Diego, "world", Now), Message(Celine, "bonjour", Now)))

    interpreter.handle("Diego wall")

    verify(display).wall(Set(Message(Diego, "hello", Now), Message(Diego, "world", Now), Message(Celine, "bonjour", Now)))
  }

  test("should display user's messages when the user name is given") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(messages.findBy(Diego)).thenReturn(mutable.Stack(Message(Diego, "world", Now), Message(Diego, "hello", Now)))

    interpreter.handle("Diego")

    verify(display).timeline(immutable.Stack(Message(Diego, "world", Now), Message(Diego, "hello", Now)))
  }
}
