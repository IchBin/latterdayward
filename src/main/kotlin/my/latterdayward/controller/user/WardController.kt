package my.latterdayward.controller.user

import io.swagger.v3.oas.annotations.Operation
import my.latterdayward.data.AccessRequest
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.data.Ward
import my.latterdayward.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/user/ward")
class WardController(
    private val userService: UserService
) {

    @GetMapping("")
    fun wardHome(model: MutableMap<String, Any?>, session: HttpSession, user: User): String {
        val userWard = userService.findUserByUserName(user.username)
        userWard?.ward?.let {
            model["ward"] = it
            model["editors"] = userService.findEditors(it.path!!)
            model["publishers"] = userService.findPublishers(it.path!!)
        } ?: run {
            model["ward"] = Ward()
            model["ward_list"] = userService.mapOfWards()
            model["accessRequest"] = AccessRequest()
        }
        return "user/ward"
    }

    @PostMapping("/save")
    fun saveWard(@ModelAttribute ward: Ward, session: HttpSession, r: RedirectAttributes, m: Messages, user: User): String {
        user.setAsWardOwner(ward)
        userService.save(user)
        r.addFlashAttribute("messages", m.success("Success!","Ward was saved."))
        return "redirect:/user/ward"
    }

    @Operation(hidden = true)
    @ResponseBody
    @GetMapping("/names")
    fun wardNamesExists(@RequestParam path: String): Boolean {
        return userService.wardExists(path)
    }
}