package domain.message.commands

import domain.message.{Message, Messages}
import domain.user.{User, Users}
import domain.{Command, CommandHandler}
import org.joda.time.DateTime

case class PostMessageCommand(userName: String, message: String, postTime: DateTime) extends Command

class PostMessageCommandHandler(messages: Messages, users: Users) extends CommandHandler[PostMessageCommand] {
  override def handle(post: PostMessageCommand) = {
    val maybeAUser = users.findByName(post.userName)
    val user = maybeAUser.getOrElse(createUser(post.userName))
    messages.add(Message(user, post.message, post.postTime))
  }

  private def createUser(userName: String): User = {
    val user = User(userName.trim)
    users.add(user)
    user
  }
}
