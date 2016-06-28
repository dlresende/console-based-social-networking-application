import org.joda.time.DateTime

import scala.collection.mutable

class Messages() {

  private val messages:mutable.Set[Message] = mutable.Set()

  def findBy(users: User*) = messages.filter(message => users.contains(message.author)).toSet

  def findBy(users: Set[User]):Set[Message] = findBy(users.toArray:_*)

  def all() = messages.toSet

  def add(newMessage: Message) = messages += newMessage

  def deleteAll() = messages.clear()
}

case class Message(author: User, content: String, creationTime:DateTime) {}
