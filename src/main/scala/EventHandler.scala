class EventHandler(messages: Messages, users: Users, display: Display) {

  def handle(event: Event): Unit = {
    event match {
      case post: PostMessage =>
        val maybeAUser = users.findByName(post.userName)
        val user = maybeAUser.getOrElse(createUser(post.userName))
        messages.add(Message(user, post.message, post.postTime))

      case follow: AddFollower =>
        val user = findUserBy(follow.user)
        val anotherUser = findUserBy(follow.followee)
        users.addFollower(user, anotherUser)

      case wall: DisplayWall =>
        val user = findUserBy(wall.user)
        val userWall = messages.findBy((users followedBy user) + user)
        display.wall(userWall)

      case read: ReadMessages =>
        val user = findUserBy(read.user)
        val userMessages = messages.findBy(user)
        display.timeline(userMessages)
    }
  }

  private def findUserBy(userName: String): User = {
    users.findByName(userName) match {
      case Some(user) =>
        user
      case None =>
        throw new RuntimeException("Oups, the user \"" + userName + "\" doesn't exist.")
    }
  }

  private def createUser(userName: String): User = {
    val user = User(userName.trim)
    users.add(user)
    user
  }
}
