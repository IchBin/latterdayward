package my.latterdayward.controller.user

import my.latterdayward.data.Agenda
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.repo.AgendaRepository
import my.latterdayward.service.PdfService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/user/agenda")
class UserAgendaController(
    private val repo: AgendaRepository,
    private val pdfService: PdfService
) {

    @GetMapping(value = ["/", ""])
    fun home(@RequestParam(required = false) page: String?, model: MutableMap<String, Any?>, user: User): String {
        val pageable = PageRequest.of((page?.toInt()?.minus(1)) ?: 0, 10)
        model["page"] = page?.toInt() ?: 1
        model["agendas"] = repo.findAllByWardPathOrderByDateDesc(user.ward?.path!!, pageable)
        return "user/agenda"
    }

    @GetMapping("/add")
    fun addAgenda(model: MutableMap<String, Any?>): String {
        model["agenda"] = Agenda().toForm()
        return "user/agenda_add"
    }

    @PostMapping("/copy")
    fun copyAgenda(@RequestParam id: String, model: MutableMap<String, Any?>): String {
        val agenda = repo.findByIdOrNull(id)
        agenda?.id = null
        model["agenda"] = agenda
        return "user/agenda_add"
    }

    @PostMapping("/edit")
    fun editAgenda(@RequestParam id: String, model: MutableMap<String, Any?>): String {
        model["agenda"] = repo.findByIdOrNull(id)
        return "user/agenda_add"
    }

    @PostMapping("/save")
    fun saveAgenda(agenda: Agenda, r: RedirectAttributes, m: Messages): String {
        repo.save(agenda)
        r.addFlashAttribute("messages", m.success("Success!","Your Sacrament agenda has been saved."))
        return "redirect:/user/agenda"
    }

    @PostMapping("/delete")
    fun deleteAgenda(@RequestParam id: String, r: RedirectAttributes, m: Messages): String {
        repo.deleteById(id)
        r.addFlashAttribute("messages", m.success("You have successfully deleted the Sacrament agenda."))
        return "redirect:/user/agenda"
    }

    @PostMapping("/delete-by-date")
    fun deleteByDate(@RequestParam days: Long, user: User, r: RedirectAttributes, m: Messages): String {
        repo.deleteByWardPathAndDateLessThan(user.ward?.path!!, LocalDate.now().minusDays(days))
        r.addFlashAttribute("messages", m.success("You have successfully deleted the Sacrament agendas older than $days."))
        return "redirect:/user/agenda"
    }

    @PostMapping("/download/{id}")
    fun downloadPdf(@PathVariable id: String, user: User, res: HttpServletResponse) {
        repo.findByIdOrNull(id)?.let {
            pdfService.generatePdf(res, it, user.ward?.title)
        }
    }
}