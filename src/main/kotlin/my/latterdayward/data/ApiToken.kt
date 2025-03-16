package my.latterdayward.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class ApiToken(
    @Id
    var id: ObjectId?,
    var token: String?,
    var expires: LocalDateTime?
) {
    constructor(): this(null, null, null)

    fun hasToken(): Boolean {
        return token != null
    }

    fun isExpired(): Boolean {
        return expires?.let { LocalDateTime.now().isAfter(expires) } != false
    }

    fun hasTokenAccess(externalToken: String): Boolean {
        return (token == externalToken && isExpired().not())
    }

    fun timePeriod(): String {
        with (Period.between(LocalDate.now(), expires?.toLocalDate())) {
            return if (LocalDateTime.now().isAfter(expires))
                "Your token is expired!"
            else
                "Expires in ${years}y, ${months}m, ${days}d"
        }
    }
}