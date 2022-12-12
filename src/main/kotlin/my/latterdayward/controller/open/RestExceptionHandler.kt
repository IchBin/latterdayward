package my.latterdayward.controller.open

import my.latterdayward.data.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    protected fun handleConflict(ex: RuntimeException?, request: WebRequest?): ResponseEntity<Any>? {
        val errors = ex?.cause?.message?.let { listOf(it) }
        val response = ErrorResponse("Argument conversion error", errors)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }
}