import org.joda.time.DateTime

import scala.collection.mutable

class Messages() {

  private val messages = mutable.LinkedHashSet[Message]()

  def findBy(users: User*) = messages.filter(message => users.contains(message.author))

  def findBy(users: Set[User]):mutable.LinkedHashSet[Message] = findBy(users.toArray:_*)

  def all() = messages.toSet

  def add(newMessage: Message) = messages += newMessage

  def deleteAll() = messages.clear()
}

case class Message(author: User, content: String, creationTime:DateTime) {}
