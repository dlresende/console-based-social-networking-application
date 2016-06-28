object Main extends App {

  override def main(args: Array[String]) {

    val interpreter = new Interpreter(new Users(), new Messages(), new Clock(), new Display())

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