package domain

trait Command {}

trait CommandHandler[Command] {
  def handle(command: Command)
}

