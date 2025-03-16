package my.latterdayward.config

import my.latterdayward.interceptor.ApiInterceptor
import my.latterdayward.service.CustomOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: CustomOauth2UserService
): HandlerInterceptor {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/oauth/**", "/css/**", "/images/**", "/script/**", "/api/**", "/static-images/**", "/open/**").permitAll()
                    .requestMatchers("/user/token/**").hasRole("OWNER")
                    .requestMatchers("/user/announcement/**").hasRole("EDITOR")
                    .requestMatchers("/user/datacard/**").hasRole("PUBLISHER")
                    .requestMatchers("/user/schedule/**").hasRole("PUBLISHER")
                    .requestMatchers("/user/agenda/**").hasRole("PUBLISHER")
                    .requestMatchers("/user/file/**").hasRole("PUBLISHER")
                    .requestMatchers("/user/transfer/**").hasRole("OWNER")
                    //.requestMatchers("/**").authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/oauth/login")
                    .userInfoEndpoint { userInfo -> userInfo.userService(userService) }
                    /*.successHandler { request, response, authentication ->
                        // Force reload user data here
                        val oauth2User = authentication.principal as User
                        val refreshedUser = userService.findUserByUserName(oauth2User.userName)
                        // Update authentication
                        // Then redirect
                        response.sendRedirect("/user/home")
                    }*/
            }
            .logout { logout -> logout.logoutUrl("/oauth/logout").invalidateHttpSession(true) }
            .build()
    }

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addInterceptors(registry: InterceptorRegistry) {
                registry.addInterceptor(ApiInterceptor(userService))
                    .addPathPatterns("/api/**")
                    .order(1)
            }
        }
    }

}