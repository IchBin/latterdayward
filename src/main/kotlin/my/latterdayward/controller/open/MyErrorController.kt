package my.latterdayward.controller.open

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.util.NestedServletException
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class MyErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(req: HttpServletRequest, model: Model): String {
        model.addAttribute("errorCode", when (req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as Int) {
            HttpStatus.UNAUTHORIZED.value() -> "401"
            HttpStatus.FORBIDDEN.value() -> "403"
            HttpStatus.NOT_FOUND.value() -> "404"
            else -> {
                val e = getException(req)
                if (e is NoEmailException) {
                    req.session.invalidate()
                    "501"
                } else {
                    println("Exception cause: ${e?.cause}")
                    println("AHHH Exception #!!@#@#$ ${e?.printStackTrace()}")
                    "500"
                }
            }
        })
        return "error/problem"
    }

    private fun getException(req: HttpServletRequest): Exception? {
        val exception = req.getAttribute(RequestDispatcher.ERROR_EXCEPTION) as Exception?
        return if (exception is NestedServletException && exception.cause != null) {
            exception.cause as Exception
        } else {
            exception
        }
    }

}
class NoEmailException(private val msg: String) : RuntimeException(msg)
