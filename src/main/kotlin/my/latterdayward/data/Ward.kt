package my.latterdayward.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

class Ward (
    @Id
    var id: ObjectId?,
    var title: String?,
    var subTitle: String?,
    var created: LocalDateTime,
    var path: String?
) {
    constructor() : this(null, null, null, LocalDateTime.now(), null)
}
