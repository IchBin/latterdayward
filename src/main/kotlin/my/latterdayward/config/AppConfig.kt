package my.latterdayward.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableAsync
@EnableScheduling
@EnableMongoRepositories("my.latterdayward.repo")
class AppConfig(private val env: Environment) : WebMvcConfigurer, AbstractMongoClientConfiguration() {

    private val host = env.getProperty("spring.mail.host")
    private val port: Int? = env.getProperty("spring.mail.port")?.toInt()

    val prodMailSender: JavaMailSenderImpl
        @Profile("prod") @Bean get() {
            val mailSender = JavaMailSenderImpl()
            mailSender.host = host
            mailSender.port = port!!
            mailSender.username = env["spring.mail.username"]
            mailSender.password = env["spring.mail.password"]
            val props = mailSender.javaMailProperties
            props["mail.transport.protocol"] = "smtp"
            props["mail.smtp.auth"] = env["spring.mail.properties.mail.smtp.auth"]
            props["mail.smtp.starttls.enable"] = env["spring.mail.properties.mail.smtp.starttls.enable"]
            return mailSender
        }

    val devMailSender: JavaMailSenderImpl
        @Profile("dev") @Bean get() {
            val mailSender = JavaMailSenderImpl()
            mailSender.host = host
            mailSender.port = port!!
            return mailSender
        }

    override fun getDatabaseName(): String {
        return "ldw"
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/static-images/**")
            .addResourceLocations(env["image.resource.static.path"])
    }

    @Bean
    fun openApi() = OpenAPI().apply {
        val securityType = "x-api-key"
        info(Info().apply {
            title = "Latter Day Ward API"
            version = "0.0.1"
            description = "An API to leverage storage of meeting times, announcements, and data cards."
            contact = Contact()
                .name("Brad Grow")
                .email("admin@latterdayward.com")
        })
        components = Components()
            .addSecuritySchemes(securityType,
                SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .description("API Key access")
                    .`in`(SecurityScheme.In.HEADER)
                    .name(securityType)
            )
        security(listOf(SecurityRequirement().addList(securityType)))
    }
}