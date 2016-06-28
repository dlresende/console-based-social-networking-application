class Interpreter(users: Users, messages: Messages) {

  val Posting = """^(.+)->(.+)$""".r
  val Wall = """^(.+)\s+wall$""".r
  val Reading = """^(.+)$""".r
  val Follows = """^(.+)\s+follows\s+(.+)$""".r

  def interpret(action: String) = {
    action match {
      case Posting(userName, message) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)
        val user: User = maybeAUser.getOrElse(users.add(User(name = userName.trim)))
        messages.add(Message(user, message.trim))
        println("Well done! Your message has been posted.")
      }

      case Follows(userName, followingUserName) => {
          users.findByName(userName.trim) match {
            case Some(user) => {
                users.findByName(followingUserName.trim) match {
                  case Some(anotherUser) => {
                    users.addFollower(user, anotherUser)
                  }
                  case None => {
                      println("Oups, the user " + followingUserName + " doesn't exist.")
                  }
                }
            }
            case None => {
                println("Oups, the user " + userName + " doesn't exist.")
            }
          }
      }

      case Wall(userName) => {
        users.findByName(userName) match {
          case Some(user) => {
            val followers = users.followers(user)
            messages.findBy(followers ++ Set(user)).foreach(message => println(message.content))
          }
          case None => println("The user " + userName + " doesn't exist.")
        }
      }

      case Reading(userName) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)

        maybeAUser match {
          case Some(user) => {
            val messagesFromUser: Set[Message] = messages.findBy(maybeAUser.get)
            messagesFromUser.foreach(message => println(message.content))
          }
          case None => println("The user " + userName + " doesn't exist.")
        }

      }


      case _ => println("Sorry, I could not understand your action.")
    }

  }
}
