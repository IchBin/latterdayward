package my.latterdayward.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Document("assignment")
class Assignment(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(implementation = String::class, type = "String", required = false)
    var id: ObjectId? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var date: LocalDate? = null,
    @field:Schema(hidden = true)
    var wardPath: String? = null,
    var title: String? = null,
    var positions: List<Position> = ArrayList(),

)

class Position (
    var positionId: Int = 0,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var dateTime: LocalDateTime? = null,
    var cancelToken: String = UUID.randomUUID().toString()
)