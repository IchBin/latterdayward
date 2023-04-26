package my.latterdayward.controller.open

import my.latterdayward.data.nextSunday
import my.latterdayward.repo.AgendaRepository
import my.latterdayward.repo.UserRepository
import my.latterdayward.service.PdfService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class PdfController(
    private val repo: AgendaRepository,
    private val userRepo: UserRepository,
    private val pdfService: PdfService
) {

    @GetMapping("/open/download/{ward}")
    fun downloadAgendaPDF(@PathVariable ward: String, req: HttpServletRequest, res: HttpServletResponse) {
        val nextSunday = LocalDate.now().nextSunday()
        val wardTitle = userRepo.findAllByWardPath(ward)?.get(0)?.ward?.title
        repo.findByWardPathAndDate(ward, nextSunday)?.let {
            pdfService.generatePdf(res, it, wardTitle)
        }
    }
}