package com.newrelic.servicebroker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment env;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
            .withUser(env.getProperty("SECURITY_USER_NAME"))
            .password("{noop}"+env.getProperty("SECURITY_USER_PASSWORD"))
            .roles("USER", "ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            //HTTP Basic authentication
            .httpBasic()
            .and()
            .authorizeRequests()

            // .antMatchers(HttpMethod.GET, "/v2/**").hasRole("USER")
            // .antMatchers(HttpMethod.GET, "/v2/**").hasRole("ADMIN")
            // .antMatchers(HttpMethod.POST, "/v2/**").hasRole("USER")
            // .antMatchers(HttpMethod.PUT, "/v2/**").hasRole("USER")
            // .antMatchers(HttpMethod.PATCH, "/v2/**").hasRole("USER")
            // .antMatchers(HttpMethod.DELETE, "/v2/**").hasRole("USER")

            .antMatchers("/v2/**").hasRole("USER")

            .and()
            .csrf().disable()
            .formLogin().disable();
    }
}
