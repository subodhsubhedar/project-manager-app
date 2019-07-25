package com.myapp.projectmanager.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ProjectManagerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ProjectManagerSecurityConfiguration.class);

	private static final String PROJECT_MNGR_ROLE = "PROJECT_MNGR";
	private static final String PROJECT_ADMIN_ROLE = "PROJECT_ADMIN";

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CorsConfigurationSource projectManagerCorsConfigSrc;

	@Bean
	public BasicAuthenticationEntryPoint getTaskMngrBasicAuthPoint() {
		return new ProjectManagerBasicAuthEntryPoint();
	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("Configuring AuthenticationManagerBuilder with inMemoryAuthentication params...");

		auth.inMemoryAuthentication().withUser("subodh").password(passwordEncoder.encode("subodh123"))
				.roles(PROJECT_MNGR_ROLE).and().withUser("admin").password(passwordEncoder.encode("admin123"))
				.roles(PROJECT_ADMIN_ROLE);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Configuring Authorization params...");

		http.cors().configurationSource(projectManagerCorsConfigSrc).and().httpBasic()
				.authenticationEntryPoint(getTaskMngrBasicAuthPoint()).realmName("PROJECT_MNGR_SECURITY")
				.and()
					.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated()
				.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/tasks").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/task/**").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.POST, "/task/add").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.PUT, "/task/update").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.DELETE, "/task/delete/**").hasAnyRole(PROJECT_ADMIN_ROLE)

				.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/users").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.POST, "/user/add").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.PUT, "/user/update").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.DELETE, "/user/delete/**").hasAnyRole(PROJECT_ADMIN_ROLE)
					
					.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/projects").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.GET, "/project/**").hasAnyRole(PROJECT_MNGR_ROLE, PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.POST, "/project/add").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.PUT, "/project/update").hasAnyRole(PROJECT_ADMIN_ROLE)
				.and()
					.authorizeRequests().antMatchers(HttpMethod.DELETE, "/project/delete/**").hasAnyRole(PROJECT_ADMIN_ROLE)
					
				.and()
					.csrf().disable().formLogin().disable();

	}

}
