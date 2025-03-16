package my.latterdayward.service

import my.latterdayward.data.AccessRequest
import my.latterdayward.data.MailMessage
import my.latterdayward.data.Role
import my.latterdayward.data.User
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class EmailService(
    private val mailSender: JavaMailSender,
    env: Environment
) {
    private val domain = env["ldw.domain"]
    private val adminEmail = env["ldw.admin.email"]
    private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy:HH:mm")

    @Async
    fun send(message: MailMessage) {
        try {
            mailSender.send(message.generate(true))
        } catch (e: MailException) {
            println("Exception sending email for ${message.to}: ${e.message}")
        }
    }

    private fun createMessage(to: String, subject: String, body: String): MailMessage {
        return MailMessage().apply {
            this.to = to
            this.subject = subject
            this.from = adminEmail!!
            this.body = body
        }
    }

    fun sendAccessRequestEmail(accessRequest: AccessRequest, user: User, owner: User) {
        val body = """User: <strong>${user.userName}</strong> has requested ${accessRequest.role} access for ${accessRequest.ward}. 
                | Login to <a href="$domain/user/ward">$domain</a> to approve or deny this request.""".trimMargin()

        send(createMessage(owner.userName, "Access Request", body))
    }

    fun sendAccessApproval(user: User, role: Role) {
        val body = """You have been approved with <strong>$role</strong> access for ${user.ward?.path}. 
                | Login to <a href="$domain">$domain</a> to update your ward content.""".trimMargin()

        send(createMessage(user.userName, "Access Request Approved", body))
    }

    fun sendAccessDeny(user: User, role: Role, path: String, owner: User) {
        val body = """You have been denied or removed as a <strong>$role</strong> for $path.
                | If you feel this is an error, you should contact the <a href="mailto:${owner.userName}">ward owner</a>.""".trimMargin()

        send(createMessage(user.userName, "Access Request Denied", body))
    }

    fun sendTokenExpirationEmail(owner: User) {
        val expires = owner.apiToken?.expires?.format(dateFormatter)
        val body = """Your API token at https://api.latterdayward.com is about to expire. 
            You'll need to login and generate a new token in order to continue using the API.
            Your Token Expires on <strong>$expires</strong>.
            """.trimMargin()

        send(createMessage(owner.userName, "Attention: Your Ward website token is about to expire", body))
    }
}