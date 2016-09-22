package domain.message.commands

import domain.display.Display
import domain.message.Messages
import domain.user.{User, Users}
import domain.{Command, CommandHandler}

case class ReadMessagesCommand(user: String) extends Command

class ReadMessagesCommandHandler(users: Users, messages: Messages, display: Display) extends CommandHandler[ReadMessagesCommand] {
  override def handle(readMessages: ReadMessagesCommand) = {
    val user = findUserBy(readMessages.user)
    val userMessages = messages.findBy(user)
    display.timeline(userMessages)
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