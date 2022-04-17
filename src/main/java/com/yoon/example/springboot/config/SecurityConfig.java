package com.yoon.example.springboot.config;

import com.yoon.example.springboot.service.oauth.CustomOidcSuccessHandler;
import com.yoon.example.springboot.service.oauth.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOidcUserService customOidcUserService;
    private final CustomOidcSuccessHandler customOidcSuccessHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .headers().frameOptions().disable()

                .and()
                .authorizeRequests()
                .antMatchers("/", "/board/read/**", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**").permitAll()

                .antMatchers("/api/v1/**", "/board/save", "/board/update/**").hasRole("USER")
                .antMatchers("/register").hasRole("NOT_ENROLLED")
                .anyRequest().authenticated()

                .and()
                .logout().logoutSuccessUrl("/")

                .and()
                .oauth2Login().successHandler(customOidcSuccessHandler)
                .userInfoEndpoint().oidcUserService(customOidcUserService);
    }

}
