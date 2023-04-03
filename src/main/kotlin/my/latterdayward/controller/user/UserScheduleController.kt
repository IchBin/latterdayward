package my.latterdayward.controller.user

import my.latterdayward.data.*
import my.latterdayward.repo.ScheduleRepository
import my.latterdayward.service.FileService
import org.bson.types.ObjectId
import org.springframework.core.env.Environment
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession
import kotlin.collections.set

@Controller
@RequestMapping("/user/schedule")
class UserScheduleController(
    private val repo: ScheduleRepository,
    private val fileService: FileService,
    private val env: Environment
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, session: HttpSession, user: User): String {
        model["schedule"] = repo.findByWardPath(user.ward?.path!!)
        model["colors"] = Colors()
        return "user/schedule"
    }

    @GetMapping("/add")
    fun add(model: MutableMap<String, Any?>, user: User): String {
        model["form"] = Schedule()
        return "user/schedule_add"
    }

    @PostMapping("/save")
    fun save(schedule: Schedule, r: RedirectAttributes, m: Messages): String {
        if (repo.existsById(schedule.id.toString())) {
            schedule.events = repo.findByIdOrNull(schedule.id.toString())?.events
        }
        repo.save(schedule)
        r.addFlashAttribute("messages", m.success("Success!", "Schedule details have been added."))
        return "redirect:/user/schedule/edit/${schedule.id}"
    }

    @GetMapping("/edit/{id}")
    fun edit(@PathVariable id: String, model: MutableMap<String, Any?>, user: User): String {
        model["files"] = fileService.fileList(user)
        model["form"] = repo.findByIdOrNull(id)
        model["colors"] = Colors()
        return "user/schedule_add"
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: String, r: RedirectAttributes, m: Messages): String {
        repo.deleteById(id)
        r.addFlashAttribute(m.success("Success!", "You have deleted the meeting schedule."))
        return "redirect:/user/schedule"
    }

    @GetMapping("/edit/{id}/{event}")
    fun editEvent(@PathVariable id: String, @PathVariable event: Int, model: MutableMap<String, Any?>, user: User): String {
        val schedule = repo.findByWardPath(user.ward?.path!!)?.first { it.id == ObjectId(id) }
        model["form"] = schedule?.events?.get(event)
        model["event_index"] = event
        model["meeting_id"] = id
        model["files"] = fileService.fileList(user)
        return "user/meeting_add"
    }

    @GetMapping("/add/{id}")
    fun addEvent(@PathVariable id: String, model: MutableMap<String, Any?>, user: User): String {
        model["form"] = Event()
        model["meeting_id"] = id
        model["is_update"] = true
        model["files"] = fileService.fileList(user)
        return "user/meeting_add"
    }

    @PostMapping("/save/{id}")
    fun saveEvent(@PathVariable id: String,
                  @RequestParam event: Int?,
                  @ModelAttribute form: Event,
                  user: User): String {

        val schedule = repo.findByWardPath(user.ward?.path!!)?.first { it.id == ObjectId(id) }!!
        event?.let {
            schedule.events?.set(event, form)
        } ?: run {
            schedule.events?.add(form)
        }

        repo.save(schedule)
        return "redirect:/user/schedule/edit/$id"
    }

    @PostMapping("/delete/{id}/{event}")
    fun deletEvent(@PathVariable id: String,
                   @PathVariable event: Int,
                   user: User): String {
        val schedule = repo.findByWardPath(user.ward?.path!!)?.first { it.id == ObjectId(id) }!!
        schedule.events?.removeAt(event)
        repo.save(schedule)
        return "redirect:/user/schedule/edit/$id"
    }

    @GetMapping("/example/{type:CONFERENCE|WARD|STAKE}")
    fun exampleSchedules(@PathVariable("type") type: ScheduleType, session: HttpSession, r: RedirectAttributes, m: Messages, user: User): String {
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