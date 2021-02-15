package com.rtrevorrow.user.config;

import com.rtrevorrow.user.authentication.AuthenticationFailureHandler;
import com.rtrevorrow.user.authentication.AuthenticationSuccessHandler;
import com.rtrevorrow.user.authentication.ServerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ServerUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] SUPER_ADMIN = new String[] {"super_admin"};
        final String[] REQUESTER = new String[] {"requester"};
        final String[] APPROVER = new String[] {"approver"};

        http
                .csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()

                // open endpoints to everyone
                .antMatchers(HttpMethod.POST, "/login*").permitAll()

                // super admin endpoints only
                .antMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority(SUPER_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(SUPER_ADMIN)
                .antMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority(SUPER_ADMIN)

                // authenticated endpoints
                .antMatchers(HttpMethod.GET, "/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/logout").authenticated()

                // only super admin can use other endpoints
                .anyRequest().hasAnyAuthority(SUPER_ADMIN)

                // login / logout details
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Autowired
    protected void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
