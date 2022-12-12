package my.latterdayward.controller.admin

import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.data.UserForm
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/admin/user")
class UserAdminController(
    val service: UserService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>) : String {
        model["users"] = service.findAll()
        return "admin/user"
    }

    @PostMapping("/update")
    fun update(form: UserForm, r: RedirectAttributes, m: Messages, session: HttpSession) : String {
        val authUser = session.getAttribute("user") as User
        val user = service.findUserByUserName(form.username)
        if (authUser.id  == user?.id) {
            r.addFlashAttribute("messages", m.error("You cannot remove admin from your own account."))
            return "redirect:/admin/user"
        }
        r.addFlashAttribute("messages", m.success("Success!","User was saved."))
        service.save(form.toUser(user!!))
        return "redirect:/admin/user"
    }
}