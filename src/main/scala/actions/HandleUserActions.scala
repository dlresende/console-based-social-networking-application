package actions

import domain.CommandGateway
import domain.display.commands.DisplayWallCommand
import domain.message.commands.{PostMessageCommand, ReadMessagesCommand}
import domain.user.commands.AddFollowerCommand
import infrastructure.Clock

class HandleUserActions(commandGateway: CommandGateway, clock: Clock) {

  val Post = """^(.+)->(.+)$""".r
  val Follow = """^(.+)\s+follows\s+(.+)$""".r
  val Wall = """^(.+)\s+wall$""".r
  val Read = """^(.+)$""".r

  // Since there are only 4 commands (simple), Open-closed is not enforced
  def handle(action: String) = {
    action match {
      case Post(user, message) =>
        commandGateway.dispatch(PostMessageCommand(user.trim, message.trim, clock.now))

      case Follow(user, followee) =>
        commandGateway.dispatch(AddFollowerCommand(user.trim, followee.trim))

      case Wall(user) =>
        commandGateway.dispatch(DisplayWallCommand(user.trim))

      case Read(user) =>
        commandGateway.dispatch(ReadMessagesCommand(user.trim))

      case _ =>
        throw new RuntimeException("Sorry, I could not understand your action.\n" +
          "There are four commands. “posting”, “reading”, etc. are not part of the commands; commands always start with the user’s name.\n" +
          "\tposting: <user name> -> <message>\n" +
          "\treading: <user name>\n" +
          "\tfollowing: <user name> follows <another user>\n" +
          "\twall: <user name> wall")
    }
  }
}
