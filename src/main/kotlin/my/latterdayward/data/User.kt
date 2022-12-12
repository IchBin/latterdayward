package my.latterdayward.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Document("users")
class User(
    @Id
    var id: ObjectId?,
    var username: String,
    var active: Boolean,
    var created: LocalDateTime,
    @Field("api_access")
    var apiToken: ApiToken?,
    var admin: Boolean,
    var apiActive: Boolean,
    var ward: Ward? = null,
    var attributes: Map<String, Any?>? = null,
    var role: Role? = null,
    var accessRequest: AccessRequest? = null
) {

    fun hasWard(): Boolean {
        return ward != null
    }

    fun accessRequested(): Boolean {
        return accessRequest != null
    }

    fun editorAccess(): Boolean {
        return ( role == Role.OWNER || role == Role.EDITOR || role == Role.PUBLISHER)
    }

    fun publisherAccess(): Boolean {
        return ( role == Role.OWNER || role == Role.PUBLISHER)
    }

    fun isOwner(): Boolean {
        return role == Role.OWNER
    }

    fun isEditor(): Boolean {
        return role == Role.EDITOR
    }

    fun isPublisher(): Boolean {
        return role == Role.PUBLISHER
    }

    fun showWardForm(): Boolean {
        return (accessRequest == null && (isOwner() || ward == null))
    }

    fun approveAccess(role: Role): User {
        this.role = role
        accessRequest = null
        return this
    }

    fun denyAccess(): User {
        accessRequest = null
        role = null
        ward = null
        apiToken = null
        return this
    }

    fun setAsWardOwner(ward: Ward): Ward {
        role = Role.OWNER
        this.ward = ward
        return ward
    }

    fun tokenExpired(): Boolean {
        val today = LocalDateTime.now()
        return today.isAfter(apiToken?.expires)
    }

    fun transferApi(from: User): User {
        apiToken = from.apiToken
        return this
    }
}

enum class Role {
    OWNER,
    EDITOR,
    PUBLISHER
}
class UserForm {
    var id: ObjectId? = null
    var username: String = ""
    var active: Boolean = false
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var created: LocalDateTime? = null
    var apiActive: Boolean = false
    var admin: Boolean = false

    fun toUser(user: User): User {
        val token = ApiToken(user.apiToken?.id, user.apiToken?.token, user.apiToken?.expires)
        return User(user.id, user.username, active, user.created,
            token, admin, apiActive, user.ward, user.attributes, user.role, user.accessRequest)
    }
}

class AccessRequest(
    var ward: String? = null,
    var role: Role? = null
)