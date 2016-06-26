import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends FunSuite with ShouldMatchers with BeforeAndAfter {

  val users = new Users
  var interpreter = new Interpreter(users)

  before {
    users.deleteAll()
  }

  test("that users should be created when they post a message for the first time") {
    interpreter.interpret("posting: Diego -> hello world")

    users.all() should have size 1
  }
}
