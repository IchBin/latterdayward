package my.latterdayward.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Document("schedule")
class Schedule(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(implementation = String::class, type = "String", required = false)
    var id: ObjectId? = null,
    @field:Schema(hidden = true)
    var wardPath: String? = null,
    var time: String? = null,
    var color: String? = null,
    var events: MutableList<Event>? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var dateOverride: LocalDate? = null
)

class Event(
    var repeats: List<Int>? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var description: String? = null,
    var image: Image? = null,
    var button: ScheduleButton? = null
) {
    fun sundays(): String? {
        return repeats?.joinToString(",")
    }
}

class ScheduleButton(
    var text: String? = null,
    var link: Link? = null
)