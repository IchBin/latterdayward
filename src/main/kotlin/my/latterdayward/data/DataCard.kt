package my.latterdayward.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("datacard")
class DataCard(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(implementation = String::class, type = "String", required = false)
    var id: ObjectId? = null,
    @field:Schema(hidden = true)
    var wardPath: String? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var paragraph: String? = null,
    var images: List<Image>? = null,
    var button: Button? = null,
    var type: String? = null,
    var active: Boolean? = null,
    var order: Int? = null
) {
    fun toForm(): DataCard {
        return DataCard(
            button = Button(null, listOf(Link())),
            images = listOf(Image()),
            active = true
        )
    }

    fun hasImage(): Boolean {
        val img = images?.firstOrNull()
        return img?.alt != "" || img.src != "" || img.height != null || img.width != null
    }
}

class Image(
    var src: String? = null,
    var alt: String? = null,
    var width: Int? = null,
    var height: Int? = null
)

class Button(
    var text: String? = null,
    var link: List<Link>? = null
)

class Link(
    var url: String? = null,
    var calendly: Boolean? = null,
    var label: String? = null,
    var external: Boolean? = null
)

class DataCardOrder {
    var datacards: List<DataCard>? = null
}