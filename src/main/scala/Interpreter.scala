import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Interpreter(users: Users, messages: Messages, clock: Clock) {

  val Help    = """^help$""".r
  val Posting = """^(.+)->(.+)$""".r
  val Follows = """^(.+)\s+follows\s+(.+)$""".r
  val Wall    = """^(.+)\s+wall$""".r
  val Reading = """^(.+)$""".r

  def handle(action: String) = {
    action match {
      case Help() =>
        println("There are four commands. “posting”, “reading”, etc. are not part of the commands; commands always start with the user’s name.\n" +
          "\tposting: <user name> -> <message>\n" +
          "\treading: <user name>\n" +
          "\tfollowing: <user name> follows <another user>\n" +
          "\twall: <user name> wall")

      case Posting(userName, message) =>
        val user = findUserBy(userName)
        messages.add(Message(user, message.trim, clock.now))

      case Follows(userName, userNameFromAnotherUser) =>
        users.findByName(userName.trim) match {
          case Some(user) =>
            users.findByName(userNameFromAnotherUser.trim) match {
              case Some(anotherUser) =>
                users.addFollower(user, anotherUser)
              case None =>
                println("Oups, the user \"" + userNameFromAnotherUser + "\" doesn't exist.")
            }
          case None =>
            println("Oups, the user \"" + userName + "\" doesn't exist.")
        }

      case Wall(userName) =>
        users.findByName(userName) match {
          case Some(user) =>
            messages
              .findBy((users followedBy user) + user)
              .foreach(display)
          case None =>
            println("Oups, the user \"" + userName + "\" doesn't exist.")
        }

      case Reading(userName) =>
        users.findByName(userName.trim) match {
          case Some(user) =>
            messages
              .findBy(user)
              .foreach(display)
          case None =>
            println("Oups, the user \"" + userName + "\" doesn't exist.")
        }

      case _ =>
        println("Sorry, I could not understand your action. Try 'help' for more information.")
    }
  }

  private def findUserBy(userName: String): User = {
    val maybeAUser = users.findByName(userName.trim)
    val user = maybeAUser.getOrElse(createUser(userName))
    user
  }

  private def createUser(userName: String): User = {
    val user = User(userName.trim)
    users.add(user)
    user
  }

  private def display(message: Message) = {
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
