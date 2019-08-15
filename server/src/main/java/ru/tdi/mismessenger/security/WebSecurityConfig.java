package ru.tdi.mismessenger.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;
import ru.tdi.mismessenger.security.jwt.JwtAuthenticationEntryPoint;
import ru.tdi.mismessenger.security.jwt.JwtAuthenticationFilter;

import java.io.IOException;

@Configuration
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .cors()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                    .and()
                .authorizeRequests()
                    .antMatchers("/app/**").permitAll()
                    .antMatchers("/console/**").permitAll()
                    .anyRequest().access("hasRole('ROLE_АДМИНИСТРАТОРЫ ДОМЕНА')")
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        BasicAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

}
