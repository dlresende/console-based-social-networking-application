class Interpreter(users: Users, messages: Messages) {

  val Posting = """^posting:(.+)->(.+)$""".r

  def interpret(action: String) = {
    action match {
      case Posting(userName, message) => {
        val maybeAUser: Option[User] = users.findBy(userName.trim)
        val user: User = maybeAUser.getOrElse(users.add(User(userName.trim)))
        messages.add(Message(user, message.trim))
      }
      case _ => throw new UnsupportedOperationException
    }

  }

}
