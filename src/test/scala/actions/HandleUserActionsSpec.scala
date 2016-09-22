package actions

import domain._
import domain.display.commands.DisplayWallCommand
import domain.message.commands.{PostMessageCommand, ReadMessagesCommand}
import domain.user.commands.AddFollowerCommand
import infrastructure.Clock
import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class HandleUserActionsSpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  private val Now = DateTime.now

  private var clock: Clock = _
  private var commandGateway: CommandGateway = _
  private var handleUserActions: HandleUserActions = _

  before {
    clock = mock[Clock]
    when(clock.now).thenReturn(Now)

    commandGateway = mock[CommandGateway]
    doNothing().when(commandGateway).dispatch(any(classOf[Command]))

    handleUserActions = new HandleUserActions(commandGateway, clock)
  }

  test("post message event should be handled") {
    handleUserActions.handle("Diego->hello world")

    verify(commandGateway).dispatch(PostMessageCommand("Diego", "hello world", Now))
  }

  test("add follower event should be handled") {
    handleUserActions.handle("Diego follows Céline")

    verify(commandGateway).dispatch(AddFollowerCommand("Diego", "Céline"))
  }

  test("display wall event should be handled") {
    handleUserActions.handle("Diego wall")

    verify(commandGateway).dispatch(DisplayWallCommand("Diego"))
  }

  test("reading user messages event should be handled") {
    handleUserActions.handle("Diego")

    verify(commandGateway).dispatch(ReadMessagesCommand("Diego"))
  }

  test("should throw exception when the action is unknown") {
    evaluating { handleUserActions.handle("") } should produce [RuntimeException] // Explicitly not testing error message here
  }
}
