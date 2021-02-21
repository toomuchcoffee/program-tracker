package de.toomuchcoffee.pt.configuration;

import de.toomuchcoffee.pt.service.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import static de.toomuchcoffee.pt.domain.entity.Role.ADMIN;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN_USERNAME = "admin";

    @Value("${pt.admin-password}")
    private String adminPassword;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .logout()
                .permitAll();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser(ADMIN_USERNAME)
                .password(passwordEncoder.encode(adminPassword))
                .authorities(ADMIN.getAuthorities());
        auth.userDetailsService(authenticatedUserService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}