package my.latterdayward.validation

import my.latterdayward.data.Announcement
import org.springframework.validation.Errors
import org.springframework.validation.beanvalidation.CustomValidatorBean
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AnnouncementValidator : CustomValidatorBean() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun supports(clazz: Class<*>): Boolean {
        return Announcement::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val a = target as Announcement
        with (a) {
            if (active == null) {
                errors.rejectValue("active", "", "You must mark the announcement active true or false")
            }
            if (dates == null) {
                errors.rejectValue("date", "", "Date must be a valid ISO date string. i.e. '2022-05-05T19:00'")
            }
            if (title.isNullOrBlank()) {
                errors.rejectValue("title", "", "The title cannot be null or blank.")
            }
        }
    }

    private fun validDate(dateString: String): Boolean {
        try {
            LocalDateTime.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            println(e)
            return false
        }
        return true
    }
}