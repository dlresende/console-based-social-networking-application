import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class UsersSpec extends FunSuite with ShouldMatchers{
  private val Diego = User("Diego")
  private val Celine = User("Céline")
  private val Sandro = User("Sandro")

  test("a user can be added") {
    val users = new Users

    users.add(Diego)

    users.all should contain (Diego)
  }

  test("all users can be deleted") {
    val users = new Users
    users.add(Diego)

    users.deleteAll()

    users.all() should be ('empty)
  }

  test("can find user by name") {
    val users = new Users
    users.add(Diego)

    val maybeUser: Option[User] = users.findByName("Diego")

    maybeUser should be (Some(Diego))
  }
  
  test("a user can follow another users") {
    val users = new Users
    users.add(Celine)
    users.add(Diego)
    users.add(Sandro)

    users.addFollower(Diego, Sandro)
    users.addFollower(Diego, Celine)

    users.followedBy(Diego) should be (Set(Celine, Sandro))
  }
}
