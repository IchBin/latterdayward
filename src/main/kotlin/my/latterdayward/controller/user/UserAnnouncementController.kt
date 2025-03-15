package my.latterdayward.controller.user

import my.latterdayward.data.AnnouncementForm
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.repo.AnnouncementRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import jakarta.servlet.http.HttpSession
import org.springframework.security.core.annotation.AuthenticationPrincipal

@Controller
@RequestMapping("/user/announcement")
class UserAnnouncementController(
    private val repo: AnnouncementRepository
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, @AuthenticationPrincipal user: User): String {
        model["announcements"] = repo.findAllByWardPath(user.ward?.path!!)?.sortedBy { a -> a.dates?.first()?.date }?.groupBy { it.type }?.entries
        return "user/announcement"
    }

    @GetMapping("/add")
    fun add(model: MutableMap<String, Any?>): String {
        model["announcement"] = AnnouncementForm()
        return "user/announcement_add"
    }

    @PostMapping("/save")
    fun save(form: AnnouncementForm, r: RedirectAttributes, m: Messages): String {
        repo.save(form.toAnnouncement())
        r.addFlashAttribute("messages", m.success("Success!","Your announcement has been saved."))
        return "redirect:/user/announcement"
    }

    @PostMapping("/delete")
    fun delete(@RequestParam id: String, r: RedirectAttributes, m: Messages): String {
        repo.deleteById(id)
        r.addFlashAttribute("messages", m.success("Success!","Your announcement has been deleted."))
        return "redirect:/user/announcement"
    }

    @PostMapping("/edit")
    fun edit(model: MutableMap<String, Any?>, @RequestParam id: String): String {
        model["announcement"] = repo.findByIdOrNull(id)
        return "user/announcement_add"
    }
}