package my.latterdayward.repo

import my.latterdayward.data.Schedule
import org.springframework.data.mongodb.repository.MongoRepository

interface ScheduleRepository: MongoRepository<Schedule, String> {
    fun findByWardPath(path: String): List<Schedule>?
}