package my.latterdayward.controller.user

import my.latterdayward.data.Messages
import my.latterdayward.data.Role
import my.latterdayward.data.User
import my.latterdayward.repo.UserRepository
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user/transfer")
class TransferController(
    private val userService: UserService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        model["ward_users"] = userService.wardUsers(user.ward?.path!!)?.filter { it.id != user.id }
        return "user/transfer"
    }

    @PostMapping("")
    fun transfer(@RequestParam transferee: String, m: Messages, r: RedirectAttributes, session: HttpSession): String {
        val user = session.getAttribute("user") as User
        // change the transferee before revoking owner of the original user
        val transferUser = userService.findUserByUserName(transferee)
        // Transferee should already be part of the ward. Only need to add role.
        userService.save(transferUser?.transferApi(user)?.approveAccess(Role.OWNER)!!)
        // Remove from current owner
        userService.save(user.denyAccess())
        r.addFlashAttribute("messages", m.success("You have successfully transferred the ward to $transferee"))
        return "redirect:/user/home"
    }
}