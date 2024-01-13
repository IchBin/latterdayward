package my.latterdayward.repo

import my.latterdayward.data.Agenda
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDate

interface AgendaRepository: PagingAndSortingRepository<Agenda, String> {
    fun findAllByWardPathOrderByDateDesc(path: String): List<Agenda>?
    fun findByWardPathAndDate(path: String, date: LocalDate): Agenda?
    fun deleteByWardPathAndDateLessThan(path: String, date: LocalDate): Long?
    fun findAllByWardPathOrderByDateDesc(path: String, pageable: Pageable): Page<Agenda>?
}