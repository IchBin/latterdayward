package my.latterdayward.repo

import my.latterdayward.data.Role
import my.latterdayward.data.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface UserRepository: MongoRepository<User, String> {
    fun findByUserName(userName: String): User
    fun existsByUserName(userName: String): Boolean
    fun findByWardPathAndRole(wardPath: String, role: Role): User?
    fun findAllByWardPath(wardPath: String): List<User>?
    fun findByAccessRequestWard(wardPath: String): List<User>?
    fun findUsersByAccessRequestWardAndAccessRequestRoleOrWardPathAndRole(ward: String, requestRole: Role, path: String, role: Role): List<User>?
    fun findByApiTokenToken(token: String) : User?
    fun findAllByApiTokenExpiresBetween(begin: LocalDateTime, end: LocalDateTime): List<User>?
}