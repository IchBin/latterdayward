package my.latterdayward.controller.user

import my.latterdayward.data.Agenda
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.repo.AgendaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/user/agenda")
class UserAgendaController(
    private val repo: AgendaRepository
) {

    @GetMapping(value = ["/", ""])
    fun home(model: MutableMap<String, Any?>, user: User): String {
        model["agendas"] = repo.findAllByWardPathOrderByDateDesc(user.ward?.path!!)
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
    fun saveAgenda(agenda: Agenda, model: MutableMap<String, Any?>, r: RedirectAttributes, m: Messages): String {
        repo.save(agenda)
        r.addFlashAttribute("messages", m.success("Success!","Your Sacrament agenda has been saved."))
        return "redirect:/user/agenda"
    }

    @PostMapping("/delete")
    fun deleteAgenda(@RequestParam id: String, model: MutableMap<String, Any?>, r: RedirectAttributes, m: Messages): String {
        model["agenda"] = repo.deleteById(id)
        r.addFlashAttribute("messages", m.success("You have successfully deleted the Sacrament agenda."))
        return "redirect:/user/agenda"
    }
}