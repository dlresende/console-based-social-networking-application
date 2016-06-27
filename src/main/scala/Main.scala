object Main extends App {

  override def main(args: Array[String]) {

    val interpreter = new Interpreter(new Users(), new Messages())

    while (true) {
      print(">> ")

      val action = readLine()

      if(action != null) interpreter.interpret(action)
    }
  }
}