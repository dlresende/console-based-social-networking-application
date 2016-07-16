import org.joda.time.DateTime
import org.mockito.Matchers._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class ActionHandlerSpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  private val Now = DateTime.now

  private var clock: Clock = _
  private var eventHandler: EventHandler = _
  private var interpreter: ActionHandler = _

  before {
    clock = mock[Clock]
    when(clock.now).thenReturn(Now)

    eventHandler = mock[EventHandler]
    doNothing().when(eventHandler).handle(any(classOf[Event]))

    interpreter = new ActionHandler(eventHandler, clock)
  }

  test("post message event should be handled") {
    interpreter.handle("Diego->hello world")

    verify(eventHandler).handle(PostMessage("Diego", "hello world", Now))
  }

  test("add follower event should be handled") {
    interpreter.handle("Diego follows Céline")

    verify(eventHandler).handle(AddFollower("Diego", "Céline"))
  }

  test("display wall event should be handled") {
    interpreter.handle("Diego wall")

    verify(eventHandler).handle(DisplayWall("Diego"))
  }

  test("reading user messages event should be handled") {
    interpreter.handle("Diego")

    verify(eventHandler).handle(ReadMessages("Diego"))
  }

  test("should throw exception when the action is unknown") {
    evaluating { interpreter.handle("") } should produce [RuntimeException] // Explicitly not testing error message here
  }
}
