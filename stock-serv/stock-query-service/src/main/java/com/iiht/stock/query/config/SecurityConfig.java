package com.iiht.stock.query.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .cors().disable()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").anonymous()
            .anyRequest().permitAll().and().formLogin();
    }
}
