package com.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.springboot.app.auth.filter.JWTAuthorizationFilter;
import com.springboot.app.auth.handler.LoginSuccessHandler;
import com.springboot.app.auth.service.JWTService;
import com.springboot.app.models.service.JpaUserDetailService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@SuppressWarnings("unused")
	@Autowired
	private LoginSuccessHandler successHandler;
	
//	@Autowired
//	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailService userDetailsService;
	
	@Autowired
	private JWTService jwtService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/listar**", "/css/**", "/js/**", "/images/**","/locale","/api/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(),this.jwtService))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(),this.jwtService))
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		//Autenticacion con JPA
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		//PARA USAR AUTENTICACION CON JDBC
//		build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
//		.usersByUsernameQuery("SELECT username,password,enabled FROM usuarios WHERE username = ?")
//		.authoritiesByUsernameQuery("SELECT u.username,r.authority FROM roles r INNER JOIN usuarios u ON r.user_id = u.id WHERE u.username = ? ");

		//PARA MANTENER UN SISTEMA DE INICIO SE SESION EN MEMORIA
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
//
//		build.inMemoryAuthentication().withUser(users.username("admin").password("123").roles("ADMIN", "USER"))
//				.withUser(users.username("alonso").password("123").roles("USER"));  
	}
}
