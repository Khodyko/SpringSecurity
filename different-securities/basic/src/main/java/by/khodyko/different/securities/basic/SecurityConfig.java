package by.khodyko.different.securities.basic;

import by.khodyko.different.securities.basic.handler.DefaultAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityWebApplicationInitializer {

    @Autowired
    private AccessDeniedHandler defaultAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                )
                .exceptionHandling((ex)->ex.accessDeniedHandler(defaultAccessDeniedHandler))
                .formLogin(login-> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/hello")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login")
                        .permitAll());
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER").build());
        manager.createUser(User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}
