class Users {

  var users:Set[User] = Set()

  def findByName(userName: String): Option[User] = users.find(user => user.name.equals(userName))

  def add(user: User): User = {
    users += user
    user
  }

  def deleteAll(): Any = {
    users = Set()
  }

  def all(): scala.collection.immutable.Set[User] = users

}
