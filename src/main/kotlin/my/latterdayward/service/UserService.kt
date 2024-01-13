package my.latterdayward.service

import my.latterdayward.controller.open.NoEmailException
import my.latterdayward.data.Role
import my.latterdayward.data.User
import my.latterdayward.repo.UserRepository
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(val repo: UserRepository) {

    fun get(authUser: DefaultOAuth2User): User {
        val userName = authUser.attributes["email"] as String? ?: throw NoEmailException("No github public email set")
        return if (repo.existsByUsername(userName)) {
            repo.findByUsername(userName)
        } else {
            repo.insert(User(id = null, username = userName, active = true, created = LocalDateTime.now(),
                apiToken = null, admin = false, apiActive = true, ward = null, attributes = authUser.attributes))
        }
    }

    fun findUserByUserName(userName: String): User? {
        return repo.findByUsername(userName)
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
        val requests = repo.findByAccessRequest_Ward(path)
        return if (requests?.isEmpty() == true) { null } else { requests }
    }

    fun findEditors(path: String): List<User>? {
        return repo.findUsersByAccessRequest_WardAndAccessRequest_RoleOrWardPathAndRole(path, Role.EDITOR, path, Role.EDITOR)
    }

    fun findPublishers(path: String): List<User>? {
        return repo.findUsersByAccessRequest_WardAndAccessRequest_RoleOrWardPathAndRole(path, Role.PUBLISHER, path, Role.PUBLISHER)
    }

    fun mapOfWards(): Map<String?, String?> {
       return findAll().associate { it.ward?.path to it.ward?.title }.filterValues { it != null }
    }

    fun wardUsers(path: String): List<User>? {
        return repo.findAllByWardPath(path)
    }

    fun expiredTokens(): List<User>? {
        val now = LocalDateTime.now()
        return repo.findAllByApiTokenExpiresBetween(now, now.plusMonths(1))
    }
}