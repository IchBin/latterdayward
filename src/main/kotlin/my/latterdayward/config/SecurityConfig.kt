package my.latterdayward.config

import my.latterdayward.interceptor.AdminInterceptor
import my.latterdayward.interceptor.ApiInterceptor
import my.latterdayward.interceptor.UserInterceptor
import my.latterdayward.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: UserService
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.authorizeHttpRequests { authz ->
                authz
                    .antMatchers("/oauth/**", "/css/**", "/images/**", "/script/**", "/api/**", "/static-images/**").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                    .oauth2Login()
                    .loginPage("/oauth/login")
                    .defaultSuccessUrl("/user/home")
                    .and()
                    .logout()
                    .logoutUrl("/oauth/logout")
                    .invalidateHttpSession(true)

            }
        return http.build()
    }

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addInterceptors(registry: InterceptorRegistry) {
                registry.addInterceptor(ApiInterceptor(userService))
                    .addPathPatterns("/api/**")
                    .order(1)
                registry.addInterceptor(AdminInterceptor())
                    .addPathPatterns("/admin/**")
                    .order(1)
                registry.addInterceptor(UserInterceptor(userService))
                    .addPathPatterns("/admin/**", "/user/**")
                    .order(0)
            }
        }
    }
}