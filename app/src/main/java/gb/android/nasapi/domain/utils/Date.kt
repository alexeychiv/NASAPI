import android.icu.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd"

fun getTodayDate(): String = SimpleDateFormat(DATE_FORMAT).format(Date())

fun getDateDaysBefore(days: Long): String {
    var dateDaysBefore = LocalDate.now().minusDays(days)

    return dateDaysBefore.toString()
}