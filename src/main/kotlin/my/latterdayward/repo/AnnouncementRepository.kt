package my.latterdayward.repo

import my.latterdayward.data.Announcement
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AnnouncementRepository: MongoRepository<Announcement, String> {
    fun findAllByWardPath(path: String): List<Announcement>?
    fun findByIdAndWardPath(id: ObjectId, path: String): Announcement?
    fun findByWardPathAndType(path: String, type: String): List<Announcement>?
}