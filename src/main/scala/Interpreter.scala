class Interpreter(users: Users) {

  val Posting = """^posting:(.+)->(.+)$""".r
  val messages = new Messages()

  def interpret(action: String) = {
    action match {
      case Posting(userName, message) => {
        val maybeAUser: Option[User] = users.findBy(userName)
        val user: User = maybeAUser.getOrElse(users.add(User(userName)))
        //messages.add(Message(message, user))
      }
      case _ => throw new UnsupportedOperationException
    }

  }

}
