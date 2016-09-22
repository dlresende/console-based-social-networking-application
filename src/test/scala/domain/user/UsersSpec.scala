package domain.user

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}

class UsersSpec extends FunSuite with ShouldMatchers with BeforeAndAfter {
  private val Diego = User("Diego")
  private val Celine = User("Céline")
  private val Sandro = User("Sandro")

  private var users: Users = _

  before {
    users = new Users
  }

  test("a user can be added") {
    users add Diego

    users.findByName("Diego") should be (Option(Diego))
  }

  test("can find user by name") {
    users add Diego

    val maybeUser: Option[User] = users.findByName("Diego")

    maybeUser should be (Some(Diego))
  }
  
  test("a user can follow another users") {
    users add Celine
    users add Diego
    users add Sandro

    users.addFollower(Diego, Sandro)
    users.addFollower(Diego, Celine)

    users.followedBy(Diego) should be (Set(Celine, Sandro))
  }
}
