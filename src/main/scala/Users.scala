import scala.collection.mutable

class Users {

  private val users =
    new mutable.HashMap[User, mutable.Set[User]]()
      with mutable.MultiMap[User, User]

  def followedBy(user: User):Set[User] = users(user).toSet

  def addFollower(user: User, follower: User) = users.addBinding(user, follower)

  def findByName(userName: String): Option[User] = users.keys.find(user => user.name == userName)

  def add(user: User): User = {
    users.+=((user, mutable.Set()))
    user
  }

  def deleteAll() = users.clear()

  def all() = users.keys
}

case class User(name: String) {}
