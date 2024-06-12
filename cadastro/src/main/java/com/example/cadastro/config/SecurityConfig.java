package com.example.cadastro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login","/usuario/cadastro","/sucesso ", "/h2-console/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();

        http.headers().frameOptions().sameOrigin();
    }
}
