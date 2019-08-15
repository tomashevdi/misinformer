package ru.tdi.mismessenger.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(1)
@EnableWebSecurity
public class JWTWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.antMatcher("/getJWT")
                .cors()
                .and()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/getJWT").permitAll()
                .anyRequest().authenticated();
    }
}
