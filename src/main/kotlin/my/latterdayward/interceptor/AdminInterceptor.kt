package my.latterdayward.interceptor

import my.latterdayward.data.User
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AdminInterceptor : HandlerInterceptor {

    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any): Boolean {
        val user = req.session.getAttribute("user") as User
        user.let {
            if (it.admin) return true
        }
        res.status = HttpServletResponse.SC_UNAUTHORIZED
        res.sendRedirect("/error")
        return false
    }
}