package org.voetsky.dispatcherBot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf().disable()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                                .antMatchers(HttpMethod.GET, "/token/csrf").permitAll()
                                .antMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                                .antMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                                .antMatchers("/v2/api-docs/**").permitAll()
                                .antMatchers("/v2/api-docs").permitAll()
                                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                                .antMatchers("/swagger-resources/*").permitAll()
                                .antMatchers("/swagger-ui/index.html").permitAll()
                                .anyRequest().authenticated()
                )

        ;
        return http.build();
    }

}
