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
            .roles("USER");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            //HTTP Basic authentication
            .httpBasic()
            .and()
            .authorizeRequests()

            // .antMatchers(HttpMethod.GET, "/actuator").hasRole("USER")
            // .antMatchers(HttpMethod.GET, "/actuator/health").hasRole("USER")
            // .antMatchers(HttpMethod.GET, "/actuator/health/**").hasRole("USER")
            // .antMatchers(HttpMethod.GET, "/v2/catalog").hasRole("USER")
            // .antMatchers(HttpMethod.PUT, "/v2/service_instances/*").hasRole("USER")
            // .antMatchers(HttpMethod.DELETE, "/v2/service_instances/*").hasRole("USER")
            // .antMatchers(HttpMethod.DELETE, "/v2/service_instances/*/service_bindings/*").hasRole("USER")
            // .antMatchers(HttpMethod.PUT, "/v2/service_instances/*/service_bindings/*").hasRole("USER")
            // .antMatchers("/error").hasRole("USER")
            // .antMatchers("/webjars/**").hasRole("USER")

            .antMatchers("/v2/**").hasRole("USER")

            .and()
            .csrf().disable()
            .formLogin().disable();
    }
}
