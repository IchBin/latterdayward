package my.latterdayward.controller.admin

import my.latterdayward.data.Messages
import my.latterdayward.data.Ward
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/admin/ward")
class WardAdminController(
    private val userService: UserService

) {

    @GetMapping("")
    fun home(model: MutableMap<String, Any?>): String {
        populate(model)
        return "admin/ward"
    }

    @GetMapping("/edit")
    fun edit(@RequestParam(required = false) path: String, model: MutableMap<String, Any?>): String {
        populate(model)
        val user = userService.findOwnerByWardPath(path)
        user?.ward?.let {
            model["editors"] = userService.findEditors(it.path!!)
            model["publishers"] = userService.findPublishers(it.path!!)
            model["owner"] = user.username
            model["ward"] = it
        }
        model["selected"] = path
        return "admin/ward"
    }

    @PostMapping("/save")
    fun saveWard(@ModelAttribute ward: Ward, session: HttpSession, r: RedirectAttributes, m: Messages): String {
        val user = userService.findOwnerByWardPath(ward.path!!)
        user?.ward = ward
        userService.save(user!!)
        r.addFlashAttribute("messages", m.success("Success!","Ward was saved."))
        return "redirect:/admin/ward/edit?path=${ward.path}"
    }

    private fun populate(model: MutableMap<String, Any?>) {
        model["wards"] = userService.mapOfWards()
    }
}