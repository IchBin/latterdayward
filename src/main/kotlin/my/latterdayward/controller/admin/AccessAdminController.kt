package my.latterdayward.controller.admin

import my.latterdayward.data.Messages
import my.latterdayward.data.Role
import my.latterdayward.service.CustomOauth2UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/access")
class AccessAdminController(private val userService: CustomOauth2UserService) {

    @PostMapping("/approve")
    fun approveAccess(@RequestParam username: String, @RequestParam owner: String, @RequestParam role: Role, r: RedirectAttributes, m: Messages): String {
        val applicant = userService.findUserByUserName(username)
        applicant?.ward = userService.findUserByUserName(owner)?.ward
        userService.save(applicant?.approveAccess(role)!!)
        r.addFlashAttribute("messages", m.success("Approved editor access for $username"))
        return "redirect:/admin/ward/edit?path=${applicant.ward?.path}"
    }

    @PostMapping("/deny")
    fun denyAccess(@RequestParam username: String, @RequestParam owner: String, r: RedirectAttributes, m: Messages): String {
        val applicant = userService.findUserByUserName(username)
        val ownerUser = userService.findUserByUserName(owner)
        applicant?.ward = ownerUser?.ward
        userService.save(applicant?.denyAccess()!!)
        r.addFlashAttribute("messages", m.success("Removed editor access for $username"))
        return "redirect:/admin/ward/edit?path=${ownerUser?.ward?.path}"
    }
}