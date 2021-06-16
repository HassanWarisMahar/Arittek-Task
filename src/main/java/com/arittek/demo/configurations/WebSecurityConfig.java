package com.arittek.demo.configurations;


import com.arittek.demo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        Cross-Site Request Forgery is an attack that forces a user to execute unwanted actions in an application they’re currently logged into.
        If the user is a normal user, a successful attack can involve state-changing requests like transferring funds or changing their email address.
        If the user has elevated permissions, a CSRF attack can compromise the entire application.

        Spring Security has excellent CSRF support that’s on by default. If you’re using Spring MVC’s <form:form> tag or Thymeleaf and @EnableWebSecurity,
        the CSRF token will automatically be added as a hidden input field.
        If you’re using a JavaScript framework like Angular or React, you will need to configure the CookieCsrfTokenRepository so JavaScript can read the cookie.

        ***********************************************************************************************************************************************

        If you’re using Angular, this is all you need to do. If you’re using React, you’ll need to read the XSRF-TOKEN cookie and send it back as an X-XSRF-TOKEN header.
        Spring Security automatically adds a secure flag to the XSRF-TOKEN cookie when the request happens over HTTPS.
        Spring Security doesn’t use the SameSite=strict flag for CSRF cookies, but it does when using Spring Session or WebFlux session handling.
        It makes sense for session cookies since it’s being used to identify the user.
        It doesn’t provide much value for CSRF cookies since the CSRF token needs to be in the request too.


        ****************************************************************************************************************

        5. Use a Content Security Policy to Prevent XSS Attacks
        Content Security Policy (CSP) is an added layer of security that helps mitigate XSS (cross-site scripting) and data injection attacks.
        To enable it, you need to configure your app to return a Content-Security-Policy header.
        You can also use a <meta http-equiv="Content-Security-Policy"> tag in your HTML page.

        Spring security provides a number of security headers by default:
        Spring Security does not add a CSP by default. You can enable the CSP header in your Spring Boot app using the configuration below.
        CSP is a good defense to prevent XSS attacks. Keep in mind that opening up your CSP to allow for a CDN often allows many very old
        and vulnerable JavaScript libraries to be accessed.
        This means using a CDN often means that you are no longer adding much value to the security of your application.
        You can test your CSP headers are working with security headers.com.


        */
        http.headers()
                .contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
        
        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()

                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**").permitAll()
                .antMatchers("/users").authenticated()
                .antMatchers("/contacts").authenticated()
                .antMatchers("/contacts/{coontactID}").authenticated()
                .antMatchers("/contacts/{coontactID}/edit").authenticated()
                .antMatchers("/contacts/{coontactID}/delete").authenticated()
                .antMatchers("/contacts/add").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .defaultSuccessUrl("/contacts")
                .and()
                .logout().logoutSuccessUrl("/").permitAll();

    }

}
