package com.example.SpringSecurityPractice.security;

import com.example.SpringSecurityPractice.auth.ApplicationUserService;
import com.example.SpringSecurityPractice.jwt.JwtConfig;
import com.example.SpringSecurityPractice.jwt.JwtSecretKey;
import com.example.SpringSecurityPractice.jwt.JwtTokenVerifier;
import com.example.SpringSecurityPractice.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService,
                                     JwtConfig jwtConfig,
                                     JwtSecretKey jwtSecretKey) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                //.csrf().disable()
               /* .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(
                        authenticationManager(),
                        jwtSecretKey, jwtConfig))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtSecretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
*/
                .authorizeRequests()
                .antMatchers("/", "index", "/login", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
                //.antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                //.antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMIN_TRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                //.httpBasic()
                .formLogin()
                .loginPage("/login")
                     .permitAll()
                     .usernameParameter("username")
                     .passwordParameter("password")
                     .defaultSuccessUrl("/courses", true)
                /*.and()
                .rememberMe()
                     .rememberMeParameter("remember-me")
                     .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                     .key("importantData")*/
                .and()
                .logout().logoutUrl("/logout")
                         .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                         .clearAuthentication(true)
                         .invalidateHttpSession(true)
                         .deleteCookies("JSESSIONID", "remember-me", "XSRF-TOKEN")
                         .logoutSuccessUrl("/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    /*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails tomBrennan = User.builder()
                .username("tom-brennan")
                .password(passwordEncoder.encode("password"))
                //.roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthority())
                .build();

        UserDetails linda = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthority())
                //.roles(ApplicationUserRole.ADMIN.name())  // ROLE_ADMIN
                .build();

        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                .authorities(ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthority())
               // .roles(ApplicationUserRole.ADMIN_TRAINEE.name())   // ROLE_ADMIN_TRAINEE
                .build();

        return new InMemoryUserDetailsManager(tomBrennan, linda, tom);
    }*/
}
