package my.latterdayward.data

import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator

/**
 * Created by Brad Grow on 2022-09-09.
 */
class MailMessage {
    var from = "no-reply@latterdayward.com"
    var model: MutableMap<String, Any> = HashMap()
    var subject = ""
    var body = ""
    var to = ""

    fun setSubject(subject: String): MailMessage {
        this.subject = subject
        return this
    }

    fun setBody(body: String): MailMessage {
        this.body = body
        return this
    }

    fun setTo(to: String): MailMessage {
        this.to = to
        return this
    }

    @JvmOverloads
    fun generate(html: Boolean = false): MimeMessagePreparator {
        return MimeMessagePreparator { mimeMessage ->
            val message = MimeMessageHelper(mimeMessage)
            message.setTo(to)
            message.setFrom(from)
            message.setReplyTo(from)

            message.setSubject(subject)
            message.setText(body, html)
        }
    }

}