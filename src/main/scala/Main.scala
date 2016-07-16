object Main extends App {

  override def main(args: Array[String]) {

    val interpreter = init

    while (true) {
      print("> ")
      val action = readLine()

      try {
        interpreter.interpret(action.trim)
      } catch {
        case exception: RuntimeException => println(exception.getMessage)
      }
    }
  }

  private def init: Interpreter = {
    val clock = new Clock
    val users = new Users
    val messages = new Messages
    val display = new Display(clock)
    val event = new EventHandler(messages, users, display)
    new Interpreter(event, clock)
  }
}