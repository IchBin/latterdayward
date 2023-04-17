package my.latterdayward.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

@Document("agenda")
class Agenda(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(implementation = String::class, type = "String", required = false)
    var id: ObjectId? = null,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    var date: LocalDate? = null,
    @field:Schema(hidden = true)
    var wardPath: String? = null,
    var presiding: ProgramItem? = ProgramItem("Presiding"),
    var conducting: ProgramItem? = ProgramItem("Conducting"),
    var organist: ProgramItem? = ProgramItem("Organist"),
    var chorister: ProgramItem? = ProgramItem("Chorister"),
    var openHymn: ProgramItem? = ProgramItem("Opening Hymn"),
    var invocation: ProgramItem? = ProgramItem("Invocation"),
    var sacramentHymn: ProgramItem? = ProgramItem("Sacrament Hymn"),
    var closingHymn: ProgramItem? = ProgramItem("Closing Hymn"),
    var benediction: ProgramItem? = ProgramItem("Benediction"),
    var programContent: List<ProgramContent>? = ArrayList(),
    var wardAnnouncement: List<WardAnnouncement>? = ArrayList()
) {
    fun nextSunday(): LocalDate {
        return LocalDate.now().nextSunday()
    }

    fun toForm(): Agenda {
        return Agenda(programContent = mutableListOf(ProgramContent()), wardAnnouncement= mutableListOf(WardAnnouncement()))
    }
}

class ProgramContent(
    var title: String? = null,
    var name: String? = null,
    var order: Int? = null
)

class ProgramItem(
    var title: String? = null,
    var name: String? = null
)

class WardAnnouncement(
    var title: String? = null,
    var description: String? = null
)