package pe.edu.upc.patitas.login.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.upc.patitas.login.security.jwt.AuthEntryPointJwt;
import pe.edu.upc.patitas.login.security.jwt.AuthTokenFilter;
import pe.edu.upc.patitas.login.security.jwt.JwtAuthenticationFilter;
import pe.edu.upc.patitas.login.security.jwt.JwtUtils;
import pe.edu.upc.patitas.login.security.service.UserDetailsServiceImpl;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthTokenFilter authTokenFilter;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /*@Autowired
  private AuthEntryPointJwt unauthorizedHandler;*/

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
                                                     PasswordEncoder passwordEncoder,
                                                     UserDetailsService userDetailsService) throws Exception {
    return httpSecurity
      .getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder)
      .and()
      .build();
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setFilterProcessesUrl("/login");
    return http
      .cors()
      .and()
      .csrf()
      .disable()
      .authorizeHttpRequests()
      //.requestMatchers("/api/auth/**").permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .httpBasic()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilter(jwtAuthenticationFilter)
      .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    var inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    inMemoryUserDetailsManager
      .createUser(User.withUsername("admin")
        .password(passwordEncoder.encode("admin"))
        .roles()
        .build());
    return inMemoryUserDetailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  /*
  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }


*/

}
