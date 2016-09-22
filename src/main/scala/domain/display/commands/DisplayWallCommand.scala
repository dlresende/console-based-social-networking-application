package domain.display.commands

import domain.display.Display
import domain.message.Messages
import domain.user.{User, Users}
import domain.{Command, CommandHandler}

case class DisplayWallCommand(user: String) extends Command

class DisplayWallCommandHandler(users: Users, messages: Messages, display: Display) extends CommandHandler[DisplayWallCommand] {
  override def handle(displayWall: DisplayWallCommand) = {
    val user = findUserBy(displayWall.user)
    val userWall = messages.findBy((users followedBy user) + user)
    display.wall(userWall)
  }

  private def findUserBy(userName: String): User = {
    users.findByName(userName) match {
      case Some(user) =>
        user
      case None =>
        throw new RuntimeException("Oups, the user \"" + userName + "\" doesn't exist.")
    }
  }
}

