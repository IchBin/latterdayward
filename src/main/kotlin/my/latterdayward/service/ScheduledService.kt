package my.latterdayward.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledService(
    val userService: UserService,
    val emailService: EmailService) {

    @Scheduled(cron = "0 0 2 * * *")
    fun emailForTokenExpiration() {
        userService.expiredTokens()?.forEach {
            println("There are expired tokens. ")
            emailService.sendTokenExpirationEmail(it)
        }?.run {
            println("There are no expired tokens. ")
        }
    }
}