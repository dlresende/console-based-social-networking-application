class Interpreter(users: Users, messages: Messages) {

  val Posting = """^(.+)->(.+)$""".r
  val Reading = """^(.+)""".r

  def interpret(action: String) = {
    action match {
      case Posting(userName, message) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)
        val user: User = maybeAUser.getOrElse(users.add(User(userName.trim)))
        messages.add(Message(user, message.trim))
        println("Well done! Your message has been posted.")
      }
      case Reading(userName) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)

        maybeAUser match {
          case Some(user) => {
            val messagesFromUser: Set[Message] = messages.findByUser(maybeAUser.get)
            messagesFromUser.foreach(message => println(message.content))
          }
          case None => println("The user " + userName + " doesn't exist.")
        }

      }
      case _ => println("Sorry, I could not understand your action.")
    }

  }

}
