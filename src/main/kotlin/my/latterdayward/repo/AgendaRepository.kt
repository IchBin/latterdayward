package my.latterdayward.repo

import my.latterdayward.data.Agenda
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface AgendaRepository: MongoRepository<Agenda, String> {
    fun findAllByWardPathOrderByDateDesc(path: String): List<Agenda>?
    fun findByWardPathAndDate(path: String, date: LocalDate): Agenda?
}