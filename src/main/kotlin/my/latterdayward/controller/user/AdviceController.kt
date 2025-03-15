package my.latterdayward.controller.user

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class AdviceController(val env: Environment) {


    @ModelAttribute("isDev")
    fun isDev(): Boolean {
        return env.activeProfiles.contains("dev")
    }
}