class ActionHandler(eventHandler: EventHandler, clock: Clock) {

  val Post = """^(.+)->(.+)$""".r
  val Follow = """^(.+)\s+follows\s+(.+)$""".r
  val Wall = """^(.+)\s+wall$""".r
  val Read = """^(.+)$""".r

  def handle(action: String) = {
    action match {
      case Post(user, message) =>
        eventHandler.handle(PostMessage(user.trim, message.trim, clock.now))

      case Follow(user, followee) =>
        eventHandler.handle(AddFollower(user.trim, followee.trim))

      case Wall(user) =>
        eventHandler.handle(DisplayWall(user.trim))

      case Read(user) =>
        eventHandler.handle(ReadMessages(user.trim))

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
