import org.joda.time.DateTime

import scala.collection.mutable

class Messages() {

  private val messages = mutable.Stack[Message]()

  def findBy(users: User*) = messages.filter(message => users.contains(message.author))

  def findBy(users: Set[User]):Iterable[Message] = findBy(users.toArray:_*)

  def all() = messages

  def add(newMessage: Message) = messages push newMessage
}

case class Message(author: User, content: String, postTime:DateTime) {}
