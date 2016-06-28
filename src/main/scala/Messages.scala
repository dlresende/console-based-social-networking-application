class Messages() {

  var messages:Set[Message] = Set()

  def findBy(users: User*):Set[Message] = messages.filter(message => users.contains(message.author))

  def all():Set[Message] = messages

  def add(newMessage: Message) = messages += newMessage

  def deleteAll() = messages = Set()
}

case class Message(author: User, content: String) {}
