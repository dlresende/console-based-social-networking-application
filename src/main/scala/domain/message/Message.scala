package domain.message

import domain.user.User
import org.joda.time.DateTime

case class Message(author: User, content: String, postTime:DateTime) {}
