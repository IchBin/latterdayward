package my.latterdayward.controller.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import jakarta.servlet.http.HttpSession

@Controller
class LoginController {

    @GetMapping("/oauth/login")
    fun getLoginPage(model: MutableMap<String, Any?>): String {
        model["urls"] = mapOf("Google" to "google", "GitHub" to "github")
        return "user/login"
    }

    @GetMapping("/oauth/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/oauth/login"
    }
}