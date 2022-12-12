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

@Component
class EmailService(
    private val mailSender: JavaMailSender,
    env: Environment
) {

    private val domain = env["ldw.domain"]

    @Async
    fun send(message: MailMessage) {
        try {
            mailSender.send(message.generate(true))
        } catch (e: MailException) {
            // runtime exception
            println("Exception sending email for ${message.to}")
            println(e)
        }
    }

    fun sendAccessRequestEmail(accessRequest: AccessRequest, user: User, owner: User) {
        val message = MailMessage()
        message.subject = "Access Request"
        message.to = owner.username
        message.body = """User: <strong>${user.username}</strong> has requested ${accessRequest.role} access for ${accessRequest.ward}. 
                | Login to <a href="$domain/user/ward">$domain</a> to approve or deny this request.""".trimMargin()
        send(message)
    }

    fun sendAccessApproval(user: User, role: Role) {
        val message = MailMessage()
        message.subject = "Access Request Approved"
        message.to = user.username
        message.body = """You have been approved with <strong>$role</strong> access for ${user.ward?.path}. 
                | Login to <a href="$domain">$domain</a> to update your ward content.""".trimMargin()
        send(message)
    }

    fun sendAccessDeny(user: User, role: Role, path: String, owner: User) {
        val message = MailMessage()
        message.subject = "Access Request Denied"
        message.to = user.username
        message.body = """You have been denied or removed as a <strong>$role</strong> for $path.
                | If you feel this is an error, you should contact the <a href="mailto:${owner.username}">ward owner</a>.""".trimMargin()
        send(message)
    }
}