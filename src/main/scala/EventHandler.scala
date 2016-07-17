class EventHandler(messages: Messages, users: Users, display: Display) {

  val postMessageObserver = new PostMessageObserver(messages, users)
  val addFollowerObserver = new AddFollowerObserver(users)
  val displayWallObserver = new DisplayWallObserver(users, messages, display)
  val readMessagesObserver = new ReadMessagesObserver(users, messages, display)

  def handle(event: Event): Unit = {
    event match {
      case post: PostMessage =>
        postMessageObserver.notify(post)

      case follow: AddFollower =>
        addFollowerObserver.notify(follow)

      case wall: DisplayWall =>
        displayWallObserver.notify(wall)

      case read: ReadMessages =>
        readMessagesObserver.notify(read)
    }
  }
}

sealed trait Observer[Event] {
  def notify(event: Event)
}

class PostMessageObserver(messages: Messages, users: Users) extends Observer[PostMessage] {
  override def notify(post: PostMessage) = {
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

class AddFollowerObserver(users: Users) extends Observer[AddFollower] {
  override def notify(addFollower: AddFollower) = {
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

class DisplayWallObserver(users: Users, messages: Messages, display: Display) extends Observer[DisplayWall] {
  override def notify(displayWall: DisplayWall) = {
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

class ReadMessagesObserver(users: Users, messages: Messages, display: Display) extends Observer[ReadMessages] {
  override def notify(readMessages: ReadMessages) = {
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