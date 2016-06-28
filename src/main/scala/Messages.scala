import org.joda.time.DateTime

class Messages() {

  var messages:Set[Message] = Set()

  def findBy(users: User*):Set[Message] = messages.filter(message => users.contains(message.author))

  def findBy(users: Iterable[User]):Set[Message] = findBy(users.toArray:_*)

  def all():Set[Message] = messages

  def add(newMessage: Message) = messages += newMessage

  def deleteAll() = messages = Set()
}

case class Message(author: User, content: String, creationTime:DateTime) {}
