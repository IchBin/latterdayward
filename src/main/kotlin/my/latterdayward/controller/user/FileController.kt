package my.latterdayward.controller.user

import my.latterdayward.data.Messages
import my.latterdayward.data.User
import my.latterdayward.service.FileService
import my.latterdayward.service.FileStorageException
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/user/file")
class FileController(
    private val fileService: FileService
) {

    @GetMapping(value = ["", "/"])
    fun home(model: MutableMap<String, Any?>, @AuthenticationPrincipal user: User): String {
        model["files"] = fileService.fileList(user)
        return "user/file"
    }

    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun upload(@RequestParam file: MultipartFile, r: RedirectAttributes, m: Messages, @AuthenticationPrincipal user: User): String {
        fileService.uploadFile(file, user)
        r.addFlashAttribute("messages", m.success("Succesfully uploaded ${file.name}"))
        return "redirect:/user/file"
    }

    @PostMapping("/delete")
    fun delete(@RequestParam imageId: String, r: RedirectAttributes, m: Messages, @AuthenticationPrincipal user: User): String {
        fileService.delete(imageId, user)
        r.addFlashAttribute("messages", m.success("Succesfully deleted $imageId"))
        return "redirect:/user/file"
    }
}

@ControllerAdvice
class ImageExceptionHandler(val env: Environment) {
    @ExceptionHandler(value = [FileStorageException::class])
    fun handleFileStorageException(exception: Exception,  r: RedirectAttributes): String {
        val messages = Messages()
        r.addFlashAttribute("messages", messages.error(exception.message ?: "File upload error"))
        return "redirect:/user/file"
    }

    @ExceptionHandler(value = [FileSizeLimitExceededException::class, SizeLimitExceededException::class])
    fun handleFileSizeException(exception: Exception,  r: RedirectAttributes): String {
        val messages = Messages()
        r.addFlashAttribute("messages", messages.error("Exceeded file size limit of ${env["spring.servlet.multipart.max-file-size"]}."))
        return "redirect:/user/file"
    }
}