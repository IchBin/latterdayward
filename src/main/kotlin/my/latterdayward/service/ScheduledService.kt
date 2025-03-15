package my.latterdayward.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledService(
    val userService: CustomOauth2UserService,
    val emailService: EmailService) {

    private val logger = LoggerFactory.getLogger(ScheduledService::class.java)

    @Scheduled(cron = "0 0 2 * * *")
    fun emailForTokenExpiration() {
        userService.expiredTokens()?.forEach {
            logger.info("There are expired tokens. ")
            emailService.sendTokenExpirationEmail(it)
        }?.run {
            logger.info("There are no expired tokens. ")
        }
    }
}