package com.iiht.stock.command.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/stock-command-service/**").permitAll()
                .antMatchers("/api/**", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**").anonymous()
                .anyRequest().permitAll();
    }
}
