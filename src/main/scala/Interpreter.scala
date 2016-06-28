import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

class Interpreter(users: Users, messages: Messages, clock: Clock) {

  val Posting = """^(.+)->(.+)$""".r
  val Follows = """^(.+)\s+follows\s+(.+)$""".r
  val Wall    = """^(.+)\s+wall$""".r
  val Reading = """^(.+)$""".r

  def interpret(action: String) = {
    action match {
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
                println("Oups, the user " + userNameFromAnotherUser + " doesn't exist.")
            }
          case None =>
            println("Oups, the user " + userName + " doesn't exist.")
        }

      case Wall(userName) =>
        users.findByName(userName) match {
          case Some(user) =>
            messages
              .findBy((users followedBy user) + user)
              .foreach(display)
          case None =>
            println("Oups, the user " + userName + " doesn't exist.")
        }

      case Reading(userName) => {
        users.findByName(userName.trim) match {
          case Some(user) =>
            messages
              .findBy(user)
              .foreach(display)
          case None =>
            println("Oups, the user " + userName + " doesn't exist.")
        }

      }

      case _ => println("Sorry, I could not understand your action.")
    }
  }

  private def findUserBy(userName: String): User = {
    val maybeAUser = users.findByName(userName.trim)
    val user = maybeAUser.getOrElse(createUser(userName))
    user
  }

  private def createUser(userName: String): User = {
    val user = User(name = userName.trim)
    users.add(user)
    user
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
