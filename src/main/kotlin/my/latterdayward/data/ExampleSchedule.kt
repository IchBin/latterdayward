package my.latterdayward.data

import org.springframework.core.env.Environment
import org.springframework.core.env.get
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

enum class ScheduleType {
    WARD, STAKE, CONFERENCE
}

class ExampleSchedule(
    env: Environment
) {
    private val domain = env["ldw.domain"]
    fun wardSchedule(user: User): List<Schedule> {
        return listOf(
            Schedule(
                wardPath = user.ward?.path,
                time = "12:00PM",
                color= "green",
                events = mutableListOf(
                    Event(listOf(1,2,3,4,5), "Sacrament", "In Person Meeting", "60 min all individuals",
                        Image(
                            src = "$domain/images/sacrament.webp\n",
                            alt = "Sacrament Image"
                        ),
                        ScheduleButton("", Link())
                    ),
                    Event(listOf(1,2,3,4,5), "Sacrament Live Stream", "Online Meeting", "60 min all individuals",
                        Image(
                            src = "$domain/images/sacrament-virtual.webp\n",
                            alt = "Sacrament Live Stream Image"
                        ),
                        ScheduleButton(
                            text = "View Live Stream",
                            link = Link(
                                url = "https://www.youtube.com/channel/"
                            )
                        )
                    )
                )
            ),
            Schedule(
                wardPath = user.ward?.path,
                time = "1:00PM",
                color= "blue",
                events = mutableListOf(
                    Event(listOf(2,4), "Relief Society", "In Person Meeting", "60 min all Adult Women", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(2,4), "Elders Quorum", "In Person Meeting", "60 min all Adult Men", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(1,2,3,4,5), "Primary", "In Person Meeting", "60 min all Primary children", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(2,4), "Young Men", "In Person Meeting", "60 min all Young Men", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(2,4), "Young Women", "In Person Meeting", "60 min all Young Women", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(1,3), "Gospel Doctrine", "In Person Meeting", "60 min all Adults", Image("", ""), ScheduleButton("", Link())),
                    Event(listOf(1,3), "Youth Sunday School", "In Person Meeting", "60 min all Youth", Image("", ""), ScheduleButton("", Link()))
                )
            )
        )
    }

    fun stakeSchedule(user: User): List<Schedule> {
        val nextMonthSunday = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        return listOf(
            Schedule(
                wardPath = user.ward?.path,
                dateOverride = nextMonthSunday,
                time = "Saturday 10:00AM and 2:00PM",
                color= "green",
                events = mutableListOf(
                    Event(listOf(1,2,3,4,5), "Leadership Session", "In Person Meeting", "Ward and Stake Leadership invited",
                        Image(
                            src = "$domain/images/sacrament.webp\n",
                            alt = "Sacrament Image"
                        ),
                        ScheduleButton("", Link())
                    ),
                    Event(listOf(1,2,3,4,5), "Adult Session", "In Person Meeting", "All Adults invited", Image("", ""), ScheduleButton("", Link()))
                )
            ),
            Schedule(
                wardPath = user.ward?.path,
                dateOverride = nextMonthSunday,
                time = "10:00AM and 2:00PM",
                color= "blue",
                events = mutableListOf(
                    Event(listOf(1,2,3,4,5), "General Session", "In Person Meeting", "All Families and Individuals", Image("", ""), ScheduleButton("", Link()))
                )
            )
        )
    }

    fun conferenceSchedule(user: User): List<Schedule> {
        val nextMonthSunday = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth()).with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        return listOf(
            Schedule(
                wardPath = user.ward?.path,
                dateOverride = nextMonthSunday,
                time = "Saturday 10:00AM, 12:00PM and 6:00PM",
                color= "green",
                events = mutableListOf(
                    Event(listOf(1,2,3,4,5), "Morning Session", "General Conference", "10:00AM Saturday",
                        Image(
                            src = "$domain/images/conference_center.jpg\n",
                            alt = "Conference Center Image"
                        ),
                        ScheduleButton("", Link())
                    ),
                    Event(listOf(1,2,3,4,5), "Afternoon Session", "General Conference", "12:00PM Saturday",
                        Image(
                            src = "$domain/images/conference_center.jpg\n",
                            alt = "Conference Center Image"
                        ),
                        ScheduleButton("", Link())),
                    Event(listOf(1,2,3,4,5), "Evening Session", "General Conference", "6:00PM Saturday",
                        Image(
                            src = "$domain/images/conference_center.jpg\n",
                            alt = "Conference Center Image"
                        ),
                        ScheduleButton("", Link()))
                )
            ),
            Schedule(
                wardPath = user.ward?.path,
                dateOverride = nextMonthSunday,
                time = "Sunday 10:00AM and 12:00PM",
                color= "blue",
                events = mutableListOf(
                    Event(listOf(1,2,3,4,5), "Morning Session", "General Conference", "10:00AM Sunday",
                        Image(
                            src = "$domain/images/conference_center.jpg\n",
                            alt = "Conference Center Image"
                        ),
                        ScheduleButton("", Link())
                    ),
                    Event(listOf(1,2,3,4,5), "Afternoon Session", "General Conference", "12:00PM Sunday",
                        Image(
                            src = "$domain/images/conference_center.jpg\n",
                            alt = "Conference Center Image"
                        ),
                        ScheduleButton("", Link()))
                )
            )
        )
    }
}