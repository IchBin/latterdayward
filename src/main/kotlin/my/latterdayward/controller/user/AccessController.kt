package my.latterdayward.controller.user

import my.latterdayward.data.AccessRequest
import my.latterdayward.data.Messages
import my.latterdayward.data.Role
import my.latterdayward.data.User
import my.latterdayward.service.EmailService
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import jakarta.servlet.http.HttpSession

@Controller
@RequestMapping("/user/access")
class AccessController(
    private val userService: UserService,
    private val emailService: EmailService
) {

    @PostMapping("/request")
    fun editorAccess(accessRequest: AccessRequest, session: HttpSession, r: RedirectAttributes, m: Messages) : String {
        val user = session.getAttribute("user") as User
        user.accessRequest = accessRequest
        userService.save(user)
        accessRequest.ward?.let {
            emailService.sendAccessRequestEmail(accessRequest, user, userService.findOwnerByWardPath(it)!!)
        }
        r.addFlashAttribute("messages", m.success("Access Requested!","You will have access when the Ward Owner approves."))
        return "redirect:/user/ward"
    }

    @PostMapping("/approve")
    fun approveAccess(@RequestParam username: String, @RequestParam role: Role, user: User, r: RedirectAttributes, m: Messages): String {
        val applicant = userService.findUserByUserName(username)
        applicant?.ward = user.ward
        userService.save(applicant?.approveAccess(role)!!)
        emailService.sendAccessApproval(applicant, role)
        r.addFlashAttribute("messages", m.success("Approved editor access for $username"))
        return "redirect:/user/ward"
    }

    @PostMapping("/deny")
    fun denyAccess(@RequestParam username: String, r: RedirectAttributes, m: Messages, owner: User): String {
        val user = userService.findUserByUserName(username)
        val role = user?.accessRequest?.role ?: user?.role
        val path = user?.ward?.path ?: owner.ward?.path
        userService.save(user?.denyAccess()!!)
        emailService.sendAccessDeny(user, role!!, path!!, owner)
        r.addFlashAttribute("messages", m.success("Removed editor access for $username"))
        return "redirect:/user/ward"
    }

    @PostMapping("/promote/{role}")
    fun promote(@RequestParam username: String, @PathVariable role: Role, r: RedirectAttributes, m: Messages): String {
        val accessUser = userService.findUserByUserName(username)
        userService.save(accessUser?.approveAccess(role)!!)
        r.addFlashAttribute("messages", m.success("Changed $username access to $role"))
        return "redirect:/user/ward"
    }
}