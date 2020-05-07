package pl.marcin.it.springapplication.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import pl.marcin.it.springapplication.exception.handler.CustomAccessDeniedHandler;
import pl.marcin.it.springapplication.model.user.User;
import pl.marcin.it.springapplication.repository.UserRepository;
import pl.marcin.it.springapplication.service.UserDetailsServiceImpl;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, UserRepository userRepository){
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/home", "/users/**", "/rates/**", "/country/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/register", "/css/**").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/home")
                .and()
                .csrf().disable()
                .headers().disable();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void uploadUsersToDB(){
        User admin = new User();
        admin.setUsername("Marcin");
        admin.setPassword(getPasswordEncoder().encode("Marcin123"));
        admin.setRole("ROLE_ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("Kamil");
        user.setPassword(getPasswordEncoder().encode("Kamil123"));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        userRepository.save(user);
    }
}
