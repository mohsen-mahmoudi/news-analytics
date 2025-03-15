package com.demo.microservice_2021.elastic.query.web.client.config;

import com.demo.microservice_2021.configdata.config.UserConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final UserConfigData userConfigData;

    public WebSecurityConfig(UserConfigData userConfigData) {
        this.userConfigData = userConfigData;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic() // Enable HTTP Basic authentication
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/**").hasRole("USER") // Restrict all endpoints to users with the USER role
                .anyRequest().fullyAuthenticated() // Ensure all requests are fully authenticated
                // different between authenticated() and fullyAuthenticated() methods is that the former does not check for remember-me authentication
                // and the latter does. If you want to ensure that the user is authenticated with a password, use fullyAuthenticated().
                .and()
                .csrf().disable() // Disable CSRF (if not needed)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable session creation
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername(userConfigData.getUsername())
                        .password(passwordEncoder().encode(userConfigData.getPassword())) // Encode the password
                        .roles(userConfigData.getRoles()) // Assign roles (e.g., "USER")
                        .build()
        );
        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
