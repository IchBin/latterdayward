package my.latterdayward.controller.user

import io.swagger.v3.oas.annotations.Operation
import my.latterdayward.data.AccessRequest
import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.data.Ward
import my.latterdayward.service.CustomOauth2UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.security.core.annotation.AuthenticationPrincipal

@Controller
@RequestMapping("/user/ward")
class WardController(
    private val userService: CustomOauth2UserService
) {

    @GetMapping("")
    fun wardHome(model: MutableMap<String, Any?>, @AuthenticationPrincipal user: User): String {
        val userWard = userService.findUserByUserName(user.userName)
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
    fun saveWard(@ModelAttribute ward: Ward, r: RedirectAttributes, m: Messages, @AuthenticationPrincipal user: User): String {
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