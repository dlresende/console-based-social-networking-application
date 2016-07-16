import org.joda.time.DateTime

trait Event {}

case class PostMessage(userName: String, message: String, postTime: DateTime) extends Event
case class AddFollower(user: String, followee: String) extends Event
case class DisplayWall(user: String) extends Event
case class ReadMessages(user: String) extends Event
