package my.latterdayward.controller.user

import my.latterdayward.data.*
import my.latterdayward.repo.ScheduleRepository
import my.latterdayward.service.FileService
import org.springframework.core.env.Environment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.set

@Controller
@RequestMapping("/user/schedule")
class UserScheduleController(
    private val repo: ScheduleRepository,
    private val fileService: FileService,
    private val env: Environment
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

    @GetMapping("/example/{type:CONFERENCE|WARD|STAKE}")
    fun exampleSchedules(@PathVariable("type") type: ScheduleType, session: HttpSession, r: RedirectAttributes, m: Messages): String {
        val user = session.getAttribute("user") as User
        val schedules = when(type) {
            ScheduleType.WARD -> ExampleSchedule(env).wardSchedule(user)
            ScheduleType.STAKE -> ExampleSchedule(env).stakeSchedule(user)
            ScheduleType.CONFERENCE -> ExampleSchedule(env).conferenceSchedule(user)
        }
        schedules.forEach {
            repo.save(it)
            r.addFlashAttribute("messages", m.success("Example $type schedule has been added."))
        }
        return "redirect:/user/schedule"
    }

}