package my.latterdayward.config

import my.latterdayward.service.CustomOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userService: CustomOauth2UserService
) {

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
                    .requestMatchers("/user/file/**").hasRole("PUBLISHER")
                    .requestMatchers("/user/transfer/**").hasRole("OWNER")
                    .requestMatchers("/**").authenticated()
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .loginPage("/oauth/login")
                    .userInfoEndpoint { userInfo -> userInfo.userService(userService) }
            }
            .logout { logout -> logout.logoutUrl("/oauth/logout").invalidateHttpSession(true) }
            .build()
    }


}