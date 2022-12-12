package my.latterdayward.repo

import my.latterdayward.data.Role
import my.latterdayward.data.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByUsername(username: String): User
    fun existsByUsername(username: String): Boolean
    fun findByWardPathAndRole(wardPath: String, role: Role): User?
    fun findAllByWardPath(wardPath: String): List<User>?
    fun findByAccessRequest_Ward(wardPath: String): List<User>?
    fun findUsersByAccessRequest_WardAndAccessRequest_RoleOrWardPathAndRole(ward: String, requestRole: Role, path: String, role: Role): List<User>?
    fun findByApiTokenToken(token: String) : User?
}