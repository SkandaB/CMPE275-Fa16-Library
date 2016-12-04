package edu.sjsu.cmpe275.lms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by SkandaBhargav on 11/26/16.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select USEREMAIL,PASSWORD, ENABLED from user where USEREMAIL=?")
                .authoritiesByUsernameQuery("select USEREMAIL,ROLE from user where USEREMAIL=?");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/user/showall").access("hasRole('ROLE_LIBRARIAN')")
        /*antMatchers("/user/showall").access("hasRole('ROLE_PATRON')")*/
                .and().formLogin();
        httpSecurity.csrf().disable();
    }

/*

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/user/showall").access("hasRole('ROLE_ADMIN')")
                .and().formLogin();
        httpSecurity.csrf().disable();
    }
*/

}


