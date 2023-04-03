package my.latterdayward.data

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


fun LocalDate.nextSunday(): LocalDate {
    val today = LocalDate.now()
    return if (today.dayOfWeek == DayOfWeek.SUNDAY) today else today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
}
