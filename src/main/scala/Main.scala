object Main extends App {

  override def main(args: Array[String]) {

    val interpreter = new Interpreter(new Users(), new Messages(), new Clock())

    while (true) {
      print(">> ")
      val action = readLine()
      interpreter.handle(action)
    }
  }
}