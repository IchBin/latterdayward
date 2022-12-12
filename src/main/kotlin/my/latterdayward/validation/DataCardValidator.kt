package my.latterdayward.validation

import my.latterdayward.data.DataCard
import org.springframework.validation.Errors
import org.springframework.validation.beanvalidation.CustomValidatorBean

class DataCardValidator : CustomValidatorBean() {

    override fun supports(clazz: Class<*>): Boolean {
        return DataCard::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val a = target as DataCard
        with (a) {
            if (active == null) {
                errors.rejectValue("active", "", "You must mark active true or false")
            }
            if (title.isNullOrBlank()) {
                errors.rejectValue("title", "", "The title cannot be null or blank.")
            }
        }
    }
}