

class Interpreter(users: Users, messages: Messages, clock: Clock, display: Display) {

  val Posting = """^(.+)->(.+)$""".r
  val Follows = """^(.+)\s+follows\s+(.+)$""".r
  val Wall = """^(.+)\s+wall$""".r
  val Reading = """^(.+)$""".r

  def handle(action: String) = {
    action match {
      case Posting(userName, message) =>
        val maybeAUser = users.findByName(userName.trim)
        val user = maybeAUser.getOrElse(createUser(userName))
        messages.add(Message(user, message.trim, clock.now))

      case Follows(userName, userNameFromAnotherUser) =>
        val user = findUserBy(userName)
        val anotherUser = findUserBy(userNameFromAnotherUser)
        users.addFollower(user, anotherUser)

      case Wall(userName) =>
        val user = findUserBy(userName)
        val userWall = messages.findBy((users followedBy user) + user)
        display.wall(userWall)

      case Reading(userName) =>
        val user = findUserBy(userName)
        val userMessages = messages.findBy(user)
        display.timeline(userMessages)

      case _ =>
        throw new RuntimeException("Sorry, I could not understand your action.\n" +
          "There are four commands. “posting”, “reading”, etc. are not part of the commands; commands always start with the user’s name.\n" +
          "\tposting: <user name> -> <message>\n" +
          "\treading: <user name>\n" +
          "\tfollowing: <user name> follows <another user>\n" +
          "\twall: <user name> wall")
    }
  }

  private def findUserBy(userName: String): User = {
    users.findByName(userName.trim) match {
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
