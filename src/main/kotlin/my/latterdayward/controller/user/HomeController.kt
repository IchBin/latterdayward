package my.latterdayward.controller.user

import my.latterdayward.data.User
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpSession

@Controller
class HomeController(private val userService: UserService) {

    @GetMapping("/user/home")
    fun home(session: HttpSession, model: MutableMap<String, Any?>): String {
        val user = session.getAttribute("user") as User
        model["access_requests"] = user.ward?.path?.let { userService.accessRequests(it) }
        return "user/home"
    }

    @GetMapping(value = ["", "/"])
    fun redirect(): String {
        return "redirect:/user/home"
    }
}