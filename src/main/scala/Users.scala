import scala.collection.mutable

class Users {

  private val users =
    new mutable.HashMap[User, mutable.Set[User]]()
      with mutable.MultiMap[User, User]

  def followedBy(user: User) = users(user).toSet

  def addFollower(user: User, follower: User) = users.addBinding(user, follower)

  def findByName(userName: String) = users.keys.find(user => user.name == userName)

  def add(user: User) = users += ((user, nobody))

  private def nobody = mutable.Set[User]()
}

case class User(name: String) {}
