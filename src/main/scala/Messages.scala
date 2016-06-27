class Messages() {

  var messages:Set[Message] = Set()

  def findByUser(user: User):Set[Message] = messages.filter(message => message.user.equals(user))

  def all():Set[Message] = messages

  def add(newMessage: Message) = messages += newMessage
}
