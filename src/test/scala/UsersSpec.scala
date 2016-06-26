import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class UsersSpec extends FunSuite with ShouldMatchers{
  val Diego = User("Diego")

  test("that a user can be added") {
    val users = new Users

    users.add(Diego)

    users.all should contain (Diego)
  }

  test("that all users can be deleted") {
    val users = new Users
    users.add(Diego)

    users.deleteAll()

    users.all() should be ('empty)
  }

  test("that can find user by name") {
    val users = new Users
    users.add(Diego)

    val maybeUser: Option[User] = users.findBy("Diego")

    maybeUser should be (Some(Diego))
  }
}
