package my.latterdayward.controller.user

import my.latterdayward.data.User
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpSession

@ControllerAdvice
class UserAdviceController {

    @ModelAttribute("user")
    fun getUser(session: HttpSession): User? {
        return session.getAttribute("user") as User?
    }

}