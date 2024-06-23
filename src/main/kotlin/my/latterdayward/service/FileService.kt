package my.latterdayward.service

import my.latterdayward.data.FileWrapper
import my.latterdayward.data.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Collectors


@Service
class FileService {

    @Value("\${image.static.path}")
    private val path: String? = null

    fun uploadFile(file: MultipartFile, user: User) {
        validateFile(file)
        val fileName = file.originalFilename
        val inputStream = file.inputStream
        val directory = "$path/${user.ward?.path}"
        try {
            Files.copy(inputStream, Paths.get("$directory/$fileName"), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            val msg = "Failed to store file $fileName"
            println("Exception $e")
            throw FileStorageException(msg, e)
        }
    }

    fun fileList(user: User): List<FileWrapper>? {
        val dir = "$path/${user.ward?.path}"
        // If it's the first time a directory will not exist. Create it!
        saveDirectory(dir)
        val files = Files.list(Paths.get(dir)).map(Path::toFile).collect(Collectors.toList())
        return files.map { FileWrapper(it.name, it.path) }
    }

    fun delete(fileName: String, user: User) {
        val path = "$path/${user.ward?.path}/$fileName"
        Files.delete(Paths.get(path))
    }

    fun validateFile(file: MultipartFile?) {
        if (file == null || file.isEmpty) {
            throw FileStorageException("The file cannot be empty.")
        }
        val validFileTypes = listOf("webp", "png", "gif", "jpg", "svg", "jpeg", "pdf")
        val fileExtension = StringUtils.getFilenameExtension(file.originalFilename)?.lowercase()
        if (!validFileTypes.contains(fileExtension)) {
            throw FileStorageException("Your file extension ($fileExtension) must be one of: ${validFileTypes.joinToString(", ")}")
        }
    }

    private fun saveDirectory(dir: String) {
        val directory = Paths.get(dir)
        if (!Files.exists(directory)) {
            Files.createDirectory(directory)
        }
    }
}

class FileStorageException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}