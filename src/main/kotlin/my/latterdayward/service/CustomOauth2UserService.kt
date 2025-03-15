package my.latterdayward.service

import my.latterdayward.data.Role
import my.latterdayward.data.User
import my.latterdayward.repo.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustomOauth2UserService(val repo: UserRepository): DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val email = oAuth2User.attributes["email"] as String
        return if(repo.existsByUserName(email)) {
            return repo.findByUserName(email)
        } else {
            return repo.insert(User(id = null, userName = email, active = true, created = LocalDateTime.now(),
                apiToken = null, admin = false, apiActive = true, ward = null, oauthAttributes = oAuth2User.attributes))
        }
    }

    fun findUserByUserName(userName: String): User? {
        return repo.findByUserName(userName)
    }

    fun findUserByApiToken(token: String): User? {
        return repo.findByApiTokenToken(token)
    }

    fun save(user: User) {
        repo.save(user)
    }

    fun findAll(): List<User> {
        return repo.findAll()
    }

    fun findOwnerByWardPath(path: String): User? {
        return repo.findByWardPathAndRole(path, Role.OWNER)
    }

    fun wardExists(path: String): Boolean {
        return repo.findAllByWardPath(path)?.isNotEmpty() ?: false
    }

    fun accessRequests(path: String): List<User>? {
        val requests = repo.findByAccessRequestWard(path)
        return if (requests?.isEmpty() == true) { null } else { requests }
    }

    fun findEditors(path: String): List<User>? {
        return repo.findUsersByAccessRequestWardAndAccessRequestRoleOrWardPathAndRole(path, Role.EDITOR, path, Role.EDITOR)
    }

    fun findPublishers(path: String): List<User>? {
        return repo.findUsersByAccessRequestWardAndAccessRequestRoleOrWardPathAndRole(path, Role.PUBLISHER, path, Role.PUBLISHER)
    }

    fun mapOfWards(): Map<String?, String?> {
        return findAll()
            .mapNotNull { it.ward?.let { ward -> ward.path to ward.title } }
            .sortedBy { it.second }
            .toMap()
    }

    fun wardUsers(path: String): List<User>? {
        return repo.findAllByWardPath(path)
    }

    fun expiredTokens(): List<User>? {
        val now = LocalDateTime.now()
        return repo.findAllByApiTokenExpiresBetween(now, now.plusMonths(1))
    }
}