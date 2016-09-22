package domain

import domain.display.commands.{DisplayWallCommand, DisplayWallCommandHandler}
import domain.display.Display
import domain.message._
import domain.message.commands.{PostMessageCommand, PostMessageCommandHandler, ReadMessagesCommand, ReadMessagesCommandHandler}
import domain.user.commands.{AddFollowerCommand, AddFollowerCommandHandler}
import domain.user.Users

class CommandGateway(messages: Messages, users: Users, display: Display) {

  val postMessageHandler = new PostMessageCommandHandler(messages, users)
  val addFollowerHandler = new AddFollowerCommandHandler(users)
  val displayWallHandler = new DisplayWallCommandHandler(users, messages, display)
  val readMessagesHandler = new ReadMessagesCommandHandler(users, messages, display)

  def dispatch(command: Command): Unit = {
    command match {
      case post: PostMessageCommand =>
        postMessageHandler.handle(post)

      case follow: AddFollowerCommand =>
        addFollowerHandler.handle(follow)

      case wall: DisplayWallCommand =>
        displayWallHandler.handle(wall)

      case read: ReadMessagesCommand =>
        readMessagesHandler.handle(read)
    }
  }
}
