package my.latterdayward.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document("announcement")
class Announcement(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(implementation = String::class, type = "String", required = false)
    var id: ObjectId? = null,
    @field:Schema(hidden = true)
    var wardPath: String? = null,
    var title: String? = null,
    var description: String? = null,
    var dates: List<SubDate>? = null,
    var active: Boolean? = null,
    var location: Location? = null,
    var type: String? = null
) {
    fun thymeleafDateTime(date: LocalDateTime?): String? {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return date?.format(format)
    }

    fun hasLink(): Boolean {
        return location?.address != null && location?.link != null
    }

    fun pastDue(): Boolean {
        return dates?.first()?.date?.let{ LocalDateTime.now().isAfter(it) } ?: false
    }
}

class Location(
    var address: String? = null,
    var link: String? = null
)

class SubDate(
    var subTitle: String? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var date: LocalDateTime? = null
)
class AnnouncementForm {
    var id: ObjectId? = null
    var wardPath: String? = null
    var title: String? = null
    var description: String? = null
    var dates: List<SubDate>? = mutableListOf(SubDate(null, LocalDateTime.now()))
    var active: Boolean? = true
    var location: Location? = null
    var type: String? = "ward"

    fun thymeleafDateTime(date: LocalDateTime?): String? {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return date?.format(format)
    }

    fun toAnnouncement() : Announcement {
        return Announcement(
            id, wardPath, title, description, dates, active, location, type
        )
    }
}