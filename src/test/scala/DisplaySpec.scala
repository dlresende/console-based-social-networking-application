import org.joda.time.DateTime
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar

class DisplaySpec extends FunSuite with ShouldMatchers with BeforeAndAfter with MockitoSugar {

  private val Now = DateTime.now

  private val Diego = User("Diego")

  private var clock: Clock = _
  private var display: Display = _

  before {
    clock = mock[Clock]
    when(clock.now).thenReturn(Now)
    display = new Display(clock)
  }

  test("should display the time the message has been posted in seconds if it has been less then 1 minute") {
    val _1_second_ago = Message(Diego, "-1", Now.minusSeconds(1))
    val _2_seconds_ago = Message(Diego, "-2", Now.minusSeconds(2))
    val _59_seconds_ago = Message(Diego, "-59", Now.minusSeconds(59))

    display.toString(_1_second_ago) should be ("-1 (1 second ago)")
    display.toString(_2_seconds_ago) should be ("-2 (2 seconds ago)")
    display.toString(_59_seconds_ago) should be ("-59 (59 seconds ago)")
  }

  test("should display the time the message has been posted in minutes if it has been 1 minute or more") {
    val _1_minute_ago = Message(Diego, "-1", Now.minusMinutes(1))
    val _2_minutes_ago = Message(Diego, "-2", Now.minusMinutes(2))

    display.toString(_1_minute_ago) should be ("-1 (1 minute ago)")
    display.toString(_2_minutes_ago) should be ("-2 (2 minutes ago)")
  }
}
