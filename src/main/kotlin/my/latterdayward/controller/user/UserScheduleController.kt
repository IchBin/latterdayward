package my.latterdayward.controller.user

import my.latterdayward.data.Messages
import my.latterdayward.data.Schedule
import my.latterdayward.data.User
import my.latterdayward.repo.ScheduleRepository
import my.latterdayward.service.FileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user/schedule")
class UserScheduleController(
    private val repo: ScheduleRepository,
    private val fileService: FileService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["schedule"] = repo.findByWardPath(user.ward?.path!!)
        return "user/schedule"
    }

    @GetMapping("/add")
    fun add(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["form"] = Schedule()
        model["files"] = fileService.fileList(user)
        return "user/schedule_add"
    }

    @PostMapping("/save")
    fun save(schedule: Schedule, r: RedirectAttributes, m: Messages): String {
        repo.save(schedule)
        r.addFlashAttribute("messages", m.success("Success!", "Schedule details have been added."))
        return "redirect:/user/schedule"
    }

    @PostMapping("/edit")
    fun edit(@RequestParam id: String, model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["files"] = fileService.fileList(user)
        model["form"] = repo.findByIdOrNull(id)
        return "user/schedule_add"
    }

    @PostMapping("/delete")
    fun delete(@RequestParam id: String, r: RedirectAttributes, m: Messages): String {
        repo.deleteById(id)
        r.addFlashAttribute(m.success("Success!", "You have deleted the meeting schedule."))
        return "redirect:/user/schedule"
    }
}