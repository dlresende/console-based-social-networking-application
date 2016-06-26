class Messages() {

  var messages:Set[Message] = Set()

  def all():scala.collection.immutable.Set[Message] = messages

  def add(newMessage: Message) = messages += newMessage
}
