package com.project.integration.web.security;

import com.project.integration.serv.services.UserService;
import com.project.integration.web.exception.CustomAccessDeniedHandler;
import com.project.integration.web.exception.CustomAuthenticationEntryPoint;
import com.project.integration.web.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final JwtFilter jwtFilter;
  private final UserService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final CustomAuthenticationEntryPoint unauthorizedHandler;

  @Autowired
  public SecurityConfig(
      JwtFilter jwtFilter,
      UserService userDetailsService,
      PasswordEncoder passwordEncoder,
      CustomAuthenticationEntryPoint unauthorizedHandler) {
    this.jwtFilter = jwtFilter;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.unauthorizedHandler = unauthorizedHandler;
  }



  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

//  @Override
//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/css/**", "/js/**");
//  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().httpBasic().disable().csrf().disable();

    http.authorizeRequests()
        .antMatchers("/", "/create_request", "/auth", "/css/**", "/js/landing.js", "/static/**")
        .permitAll()

        .antMatchers("/employee/new","/orders/{\\d+}/block", "/user/{\\d+}/block", "/user/{\\d+}/deactivate")
        .hasRole("ADMIN")

        .antMatchers( "/orders/all", "/orders/{\\d+}/description", "/project/create/{\\d+}")
        .hasAnyRole("ADMIN", "MANAGER")

        .antMatchers(HttpMethod.PUT, "/project/tickets/{\\d+}")
        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
        .antMatchers(HttpMethod.POST, "/project/{\\d+}/tickets")
        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
        .antMatchers("/project/reorder")
        .hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

        .anyRequest()
        .authenticated()
        .and();

    http.exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .accessDeniedHandler(accessDeniedHandler())
        .and();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
