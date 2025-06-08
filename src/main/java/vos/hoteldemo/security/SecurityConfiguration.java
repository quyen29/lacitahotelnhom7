package vos.hoteldemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vos.hoteldemo.service.AccountService;

@Configuration
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(AccountService accountService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/admin/**")
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/admin/roomBooked/updateBooking/faceRecognize")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin",
                                "/admin/authenticateTheUser",
                                "/static/**",
                                "/templates/**",
                                "/pythonServer/**",
                                "/admin/roomBooked/updateBooking/faceRecognize",
                                "/admin/login/**"
                        ).permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin")
                        .loginProcessingUrl("/admin/authenticateTheUser")
                        .usernameParameter("accountID")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/home", true)
                        .successHandler(new AdminAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin")
                        .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/showPage403"));
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain customerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(
                                "/feedback/**",
                                "/bookingHistory/**",
                                "/booking/**",
                                "/profile/**"
                        ).hasRole("CUSTOMER")
                        .requestMatchers(
                                "/",
                                "/contact/**",
                                "/promotion/**",
                                "/register/**",
                                "/verifyEmail",
                                "/showLoginPage",
                                "/authenticateTheUser",
                                "/images/**",
                                "/video/**",
                                "/customer/**",
                                "/static/**",
                                "/templates/**",
                                "/pythonServer/**",
                                "/error/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/showLoginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .usernameParameter("accountID")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/showPage403"));
        return httpSecurity.build();
    }
}
