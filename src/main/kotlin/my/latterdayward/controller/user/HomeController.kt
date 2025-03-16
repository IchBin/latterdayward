package my.latterdayward.controller.user

import my.latterdayward.data.User
import my.latterdayward.service.CustomOauth2UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(private val userService: CustomOauth2UserService) {

    @GetMapping("/user/home")
    fun home(model: MutableMap<String, Any?>, @AuthenticationPrincipal user: User): String {

        user.ward?.path?.let {
            val owner = userService.findOwnerByWardPath(it)
            model["access_requests"] = userService.accessRequests(it)
            model["owner_user"] = owner?.userName
            model["owner_name"] = owner?.attributes?.get("name")
        }
        return "user/home"
    }

    @GetMapping(value = ["", "/"])
    fun redirect(): String {
        return "redirect:/user/home"
    }
}