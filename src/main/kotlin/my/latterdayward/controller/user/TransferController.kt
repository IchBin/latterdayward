package my.latterdayward.controller.user

import my.latterdayward.data.Messages
import my.latterdayward.data.Role
import my.latterdayward.data.User
import my.latterdayward.service.CustomOauth2UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/user/transfer")
class TransferController(
    private val userService: CustomOauth2UserService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>, @AuthenticationPrincipal user: User): String {
        model["ward_users"] = userService.wardUsers(user.ward?.path!!)?.filter { it.id != user.id }
        return "user/transfer"
    }

    @PostMapping("")
    fun transfer(@RequestParam transferee: String, m: Messages, r: RedirectAttributes, @AuthenticationPrincipal user: User): String {
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