package domain.message

import domain.user.User

import scala.collection.mutable

class Messages() {

  private val messages = mutable.Stack[Message]()

  def findBy(users: User*) = messages.filter(message => users.contains(message.author))

  def findBy(users: Set[User]):Iterable[Message] = findBy(users.toArray:_*)

  def add(newMessage: Message) = messages push newMessage
}
