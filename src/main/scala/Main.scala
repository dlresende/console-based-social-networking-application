object Main extends App {

  override def main(args: Array[String]) {

    val clock = new Clock()
    val interpreter = new Interpreter(new Users(), new Messages(), clock, new Display(clock))

    while (true) {
      print(">> ")
      val action = readLine()

      try {
        interpreter.handle(action.trim)
      } catch {
        case exception: RuntimeException => println(exception.getMessage)
      }
    }
  }
}