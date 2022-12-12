package my.latterdayward.controller.admin

import my.latterdayward.data.Messages
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/token")
class TokenAdminController(
    private val userService: UserService
) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>): String {
        model["wards"] = userService.mapOfWards()
        return "admin/token"
    }

    @GetMapping("/view")
    fun view(@RequestParam path: String, model: MutableMap<String, Any?>) : String {
        model["selected"] = path
        model["wards"] = userService.mapOfWards()
        model["user"] = userService.findOwnerByWardPath(path)
        return "admin/token"
    }

    @PostMapping("/delete")
    fun delete(@RequestParam path: String, r: RedirectAttributes, m: Messages): String {
        val user = userService.findOwnerByWardPath(path)
        user?.apiToken = null
        userService.save(user!!)
        r.addFlashAttribute("messages", m.success("Success!","Token was deleted."))
        return "redirect:/admin/token"
    }
}