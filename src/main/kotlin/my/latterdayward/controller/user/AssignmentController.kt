package my.latterdayward.controller.user

import org.springframework.stereotype.Controller

@Controller
class AssignmentController {

    fun home(): String {
        return "user/assignment"
    }
}