import actions.HandleUserActions
import domain._
import domain.display.Display
import domain.message.Messages
import domain.user.Users
import infrastructure.Clock

object Cwitter extends App {

  override def main(args: Array[String]) {

    val actionHandler = init

    while (true) {
      print("> ")
      val action = readLine()

      try {
        actionHandler.handle(action.trim)
      } catch {
        case exception: RuntimeException => println(exception.getMessage)
      }
    }
  }

  private def init: HandleUserActions = {
    val clock = new Clock
    val users = new Users
    val messages = new Messages
    val display = new Display(clock)
    val event = new CommandGateway(messages, users, display)
    new HandleUserActions(event, clock)
  }
}