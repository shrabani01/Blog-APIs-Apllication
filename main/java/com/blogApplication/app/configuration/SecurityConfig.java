package com.blogApplication.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.blogApplication.app.security.CustomUserDetailService;
import com.blogApplication.app.security.JwtAuthenticationEntryPoint;
import com.blogApplication.app.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {
	
	public static final String[] PUBLIC_URLS = {"/api/auth/**" ,"/v3/api-docs","/v2/api-docs",
			"/swagger-resources/**","/swagger-ui/**","/webjars/**" ,"/error"};
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	 @Bean
     public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
     httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
             .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(PUBLIC_URLS)
            		 .permitAll()
                     .requestMatchers(HttpMethod.GET)
                     .permitAll()
                     .anyRequest().authenticated() // All other endpoints need authentication
             )
             .exceptionHandling(exception -> exception
            	        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
            	)
//             .httpBasic(withDefaults()); // Enable form-based login

             .sessionManagement(session -> session
     				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session for JWT
     			);
     
 	httpSecurity.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

     return httpSecurity.build();
 }

	 
	  @Bean
	    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
	        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
	                .authenticationProvider(daoAuthenticationProvider()) // Use DAO authentication provider
	                .build();
	    }
	  
	  @Bean
	    public DaoAuthenticationProvider daoAuthenticationProvider() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(customUserDetailService); // Custom user details service
	        provider.setPasswordEncoder(passwordEncoder()); // Password encoder
	        return provider;
	    }
	  
	  @Bean
	    public PasswordEncoder passwordEncoder(){
	        return  new BCryptPasswordEncoder();
	    }

}
