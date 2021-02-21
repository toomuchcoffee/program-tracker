package de.toomuchcoffee.pt.configuration;

import de.toomuchcoffee.pt.service.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticatedUserService authenticatedUserService;

	private static final PasswordEncoder PASSWORD_ENCODER = new CustomPasswordEncoder(); // FIXME use BCrypt

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

	@Override
	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(authenticatedUserService)
				.passwordEncoder(PASSWORD_ENCODER)
				;
	}

	private static class CustomPasswordEncoder implements PasswordEncoder {
		@Override
		public String encode(CharSequence charSequence) {
			return charSequence.toString();
		}

		@Override
		public boolean matches(CharSequence charSequence, String s) {
			return s != null && s.equals(charSequence.toString());
		}
	}
}