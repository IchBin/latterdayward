package my.latterdayward.interceptor

import my.latterdayward.service.CustomOauth2UserService
import org.springframework.web.servlet.HandlerInterceptor

class UserInterceptor(
    private val userService: CustomOauth2UserService
) : HandlerInterceptor {

//    override fun preHandle(req: HttpServletRequest, res: HttpServletResponse, handler: Any): Boolean {
//        val authUser = SecurityContextHolder.getContext().authentication.principal as DefaultOAuth2User
//        val user = userService.get(authUser)
//        req.session.setAttribute("user", user)
//        val path = req.servletPath
//        if (!user.active) {
//            res.sendError(HttpServletResponse.SC_FORBIDDEN)
//            return false
//        }
//        val noTokenAccess = path.contains("token") && !user.isOwner()
//        val noAnnouncementAccess = path.contains("announcement") && !user.editorAccess()
//        val noDataCardAccess = path.contains("datacard") && !user.publisherAccess()
//        val noScheduleAccess = path.contains("schedule") && !user.publisherAccess()
//        val noFileAccess = path.contains("file") && !user.publisherAccess()
//        val noTransferAccess = path.contains("transfer") && !user.isOwner()
//        if (noTokenAccess || noAnnouncementAccess || noDataCardAccess || noScheduleAccess || noFileAccess || noTransferAccess) {
//            res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//            return false
//        }
//        return true
//    }
}