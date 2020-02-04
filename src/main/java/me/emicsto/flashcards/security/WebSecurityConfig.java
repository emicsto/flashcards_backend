package me.emicsto.flashcards.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public static final String SIGN_IN_PATH = "/api/auth/token/sign-in";
    public static final String REFRESH_TOKEN_PATH = "/api/auth/token/refresh";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.addFilterAfter(jwtAuthorizationFilter, BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(SIGN_IN_PATH, REFRESH_TOKEN_PATH).permitAll()
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
