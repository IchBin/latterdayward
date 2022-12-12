package my.latterdayward.controller.user

import my.latterdayward.data.ApiToken
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user/token")
class TokenController(
    private val userService: UserService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        user.ward?.let {
            model["token"] = user.apiToken ?: ApiToken()
        } ?: run {
            model["token"] = ApiToken()
            model["messages"] = Messages().warning("Sorry!", "You need to create a ward before you can generate an API token.")
        }
        return "user/token"
    }

    @PostMapping("/create")
    fun createToken(session: HttpSession, r: RedirectAttributes, m: Messages): String {
        val user = session.getAttribute("user") as User
        user.apiToken = ApiToken(null, token = generateToken(), expires = LocalDateTime.now().plusYears(2))
        userService.save(user)
        r.addFlashAttribute("messages", m.success("Success!", "A token has been created."))
        return "redirect:/user/token"
    }

    @PostMapping("/delete")
    fun deleteToken(session: HttpSession, r: RedirectAttributes, m: Messages): String {
        val user = session.getAttribute("user") as User
        user.apiToken = null
        userService.save(user)
        r.addFlashAttribute("messages", m.success("Success!", "A token has been deleted."))
        return "redirect:/user/token"
    }

    private fun generateToken(): String {
        return UUID.randomUUID().toString()
    }
}