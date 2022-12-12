package my.latterdayward.validation

import my.latterdayward.data.Schedule
import org.springframework.validation.Errors
import org.springframework.validation.beanvalidation.CustomValidatorBean

class ScheduleValidator : CustomValidatorBean() {

    override fun supports(clazz: Class<*>): Boolean {
        return Schedule::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val a = target as Schedule
        with (a) {
            if (time.isNullOrBlank()) {
                errors.rejectValue("time", "", "You must set a time for this schedule.")
            }
            if (events?.isNotEmpty() == true) {
                events?.forEachIndexed { i, e ->
                    if(e.title.isNullOrBlank()) {
                        errors.rejectValue("events[$i].title", "", "Events must have a title.")
                    }
                }
            }
        }
    }

}