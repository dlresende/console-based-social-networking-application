package domain

import domain.display.Display
import domain.display.commands.DisplayWallCommand
import domain.message.commands.{PostMessageCommand, ReadMessagesCommand}
import domain.message.{Message, Messages}
import domain.user.commands.AddFollowerCommand
import domain.user.{User, Users}
import org.joda.time.DateTime
import org.mockito.Mockito.{verify, when}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.Option._
import scala.collection.{immutable, mutable}

class CommandGatewaySpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  val Now = DateTime.now
  val Celine = User("Céline")
  val Diego = User("Diego")

  var users: Users = _
  var messages: Messages = _
  var display: Display = _
  var commandGateway: CommandGateway = _

  before {
    users = mock[Users]
    display = mock[Display]
    messages = mock[Messages]
    commandGateway = new CommandGateway(messages, users, display)
  }

  test("post message event should persist messages") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))

    commandGateway.dispatch(PostMessageCommand("Diego", "message", Now))

    verify(messages).add(Message(Diego, "message", Now))
  }

  test("users should be created when he posts a message for the first time") {
    when(users.findByName("nonExistingUser")).thenReturn(empty)

    commandGateway.dispatch(PostMessageCommand("nonExistingUser", "message", Now))

    verify(users).add(User("nonExistingUser"))
  }

  test("users can follow each other") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(users.findByName("Céline")).thenReturn(Option(Celine))

    commandGateway.dispatch(AddFollowerCommand("Diego", "Céline"))

    verify(users).addFollower(Diego, Celine)
  }

  test("an exception should be thrown when a user that doesn't exist tries to follow another user") {
    evaluating { commandGateway.dispatch(AddFollowerCommand("NonExistingUser", "Diego")) } should produce [RuntimeException]
  }

  test("an exception should be thrown when a user tries to follow another user that doesn't exist") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))

    evaluating { commandGateway.dispatch(AddFollowerCommand("Diego", "NonExistingUser")) } should produce [RuntimeException]
  }

  test("an exception should be thrown when someone tries to display the wall of a user that doesn't exist") {
    evaluating { commandGateway.dispatch(DisplayWallCommand("NonExistingUser")) } should produce [RuntimeException]
  }

  test("user's wall should display his messages and messages from people he follows") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(users.followedBy(Diego)).thenReturn(Set(Celine))
    when(messages.findBy(Set(Diego, Celine))).thenReturn(Set(Message(Diego, "hello", Now), Message(Diego, "world", Now), Message(Celine, "bonjour", Now)))

    commandGateway.dispatch(DisplayWallCommand("Diego"))

    verify(display).wall(Set(Message(Diego, "hello", Now), Message(Diego, "world", Now), Message(Celine, "bonjour", Now)))
  }

  test("should display user's messages when the user name is given") {
    when(users.findByName("Diego")).thenReturn(Option(Diego))
    when(messages.findBy(Diego)).thenReturn(mutable.Stack(Message(Diego, "world", Now), Message(Diego, "hello", Now)))

    commandGateway.dispatch(ReadMessagesCommand("Diego"))

    verify(display).timeline(immutable.Stack(Message(Diego, "world", Now), Message(Diego, "hello", Now)))
  }
}
