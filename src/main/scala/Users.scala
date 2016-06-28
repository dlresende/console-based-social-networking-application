import scala.collection.mutable

class Users {

  val users =
    new mutable.HashMap[User, mutable.Set[User]]()
      with mutable.MultiMap[User, User]

  def followers(user: User):Iterable[User] = users(user)

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
