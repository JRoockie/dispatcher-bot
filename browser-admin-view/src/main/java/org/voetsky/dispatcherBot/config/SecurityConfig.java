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
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf().disable()
//                .cors().and()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((req) ->
                        req
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/token/csrf").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                                .requestMatchers("/v2/api-docs/**").permitAll()
                                .requestMatchers("/v2/api-docs").permitAll()
                                .requestMatchers("/swagger-resources/configuration/ui").permitAll()
                                .requestMatchers("/swagger-resources/*").permitAll()
                                .requestMatchers("/swagger-ui/index.html").permitAll()
                                .anyRequest().authenticated()
                )
        ;
        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://94.198.221.250", "https://94.198.221.250", "http://94.198.221.250:3000", "https://94.198.221.250:3000", "https://records-bot.ru", "http://records-bot.ru", "http://localhost:3000"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Origin", "X-Requested-With", "Content-Type", "Accept"));
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}
