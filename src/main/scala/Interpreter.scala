import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Interpreter(users: Users, messages: Messages, clock: Clock) {

  val Posting = """^(.+)->(.+)$""".r
  val Follows = """^(.+)\s+follows\s+(.+)$""".r
  val Wall    = """^(.+)\s+wall$""".r
  val Reading = """^(.+)$""".r

  def interpret(action: String) = {
    action match {
      case Posting(userName, message) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)
        val user: User = maybeAUser.getOrElse(users.add(User(name = userName.trim)))
        messages.add(Message(user, message.trim, clock.now))
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
            val followers = users.followedBy(user)
            messages.findBy(followers + user).foreach(message => display(message))
          }
          case None => println("The user " + userName + " doesn't exist.")
        }
      }

      case Reading(userName) => {
        val maybeAUser: Option[User] = users.findByName(userName.trim)

        maybeAUser match {
          case Some(user) => {
            val messagesFromUser: Set[Message] = messages.findBy(maybeAUser.get)
            messagesFromUser.foreach(message => display(message))
          }
          case None => println("The user " + userName + " doesn't exist.")
        }

      }

      case _ => println("Sorry, I could not understand your action.")
    }

  }

  private def display(message: Message): Unit = {
    val period = new Period(message.creationTime, clock.now)

    val formatter = new PeriodFormatterBuilder()
      .appendPrefix("(")
      .appendMinutes().appendSuffix(" minutes ago")
      .appendSuffix(")")
      .printZeroNever()
      .toFormatter

    println(message.content + " " + formatter.print(period))
  }
}
