package my.latterdayward.controller.user

import my.latterdayward.data.User
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import jakarta.servlet.http.HttpSession

@ControllerAdvice
class UserAdviceController(val env: Environment) {

    @ModelAttribute("user")
    fun getUser(session: HttpSession): User? {
        return session.getAttribute("user") as User?
    }

    @ModelAttribute("isDev")
    fun isDev(): Boolean {
        return env.activeProfiles.contains("dev")
    }
}