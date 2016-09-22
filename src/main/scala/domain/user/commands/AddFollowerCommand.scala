package domain.user.commands

import domain.{Command, CommandHandler}
import domain.user.{User, Users}

case class AddFollowerCommand(user: String, followee: String) extends Command

class AddFollowerCommandHandler(users: Users) extends CommandHandler[AddFollowerCommand] {
  override def handle(addFollower: AddFollowerCommand) = {
    val user = findUserBy(addFollower.user)
    val anotherUser = findUserBy(addFollower.followee)
    users.addFollower(user, anotherUser)
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
