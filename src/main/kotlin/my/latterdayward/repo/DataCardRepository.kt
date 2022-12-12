package my.latterdayward.repo

import my.latterdayward.data.DataCard
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DataCardRepository: MongoRepository<DataCard, String> {
    fun findByWardPath(path: String): List<DataCard>?
    fun findByWardPathAndType(path: String, type: String): List<DataCard>?
    fun findByIdAndWardPath(id: ObjectId, path: String): DataCard?
}