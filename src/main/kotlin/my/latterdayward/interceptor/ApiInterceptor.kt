package my.latterdayward.interceptor

import my.latterdayward.service.CustomOauth2UserService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import my.latterdayward.controller.open.ForbiddenException
import my.latterdayward.controller.open.RestExceptionHandler
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ApiInterceptor(
    private val userService: CustomOauth2UserService
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("x-api-key") ?: return false.also { throw ForbiddenException("You must provide a valid API key") }
        val user = userService.findUserByApiToken(token) ?: return false.also { throw ForbiddenException("You must provide a valid API key") }
        if(user.apiActive && user.apiToken?.hasTokenAccess(token) == true) {
            return true
        }
        throw ForbiddenException("You must provide a valid API key. Check if your key is expired.")
        return false
    }
}