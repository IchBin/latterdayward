package my.latterdayward.interceptor

import my.latterdayward.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiInterceptor(
    private val userService: UserService
): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val path = request.servletPath.split("/")[3]
        val token = request.getHeader("x-api-key")
        if (token.isNullOrBlank()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return false
        }

        val adminUser = userService.findUserByApiToken(token)
        if(adminUser?.admin == true && token == adminUser.apiToken?.token) {
            return true
        }

        val user = userService.findOwnerByWardPath(path)
        val revokedAccess = user?.apiActive?.not() ?: true
        if (revokedAccess || token != user?.apiToken?.token || user.tokenExpired()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return false
        }
        return true
    }
}