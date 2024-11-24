// package backend.example.backend.Config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import backend.example.backend.Security.jwt.JWTTokenFilter;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @Configuration
// public class SecurityConfig {

//     private final JWTTokenFilter jwtTokenFilter;

//     // Constructor injection for JWTTokenFilter
//     public SecurityConfig(JWTTokenFilter jwtTokenFilter) {
//         this.jwtTokenFilter = jwtTokenFilter;
//     }

//     // Define the SecurityFilterChain bean
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf().disable() // Disable CSRF for simplicity in APIs
//         .authorizeHttpRequests(authz -> authz
//             .requestMatchers("/auth/**").permitAll()
//             .requestMatchers(HttpMethod.POST, "/shifts/schedule").hasAuthority("ADMIN") // Corrected to hasAuthority instead of hasRole
//             .anyRequest().authenticated()
//         )
//         .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT filter

//         return http.build(); // Build and return the HttpSecurity configuration
//     }

//     // Define PasswordEncoder bean
//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // Define AuthenticationManager bean
//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }
// }

package backend.example.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import backend.example.backend.Security.jwt.JWTTokenFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
public class SecurityConfig {

    private final JWTTokenFilter jwtTokenFilter;

    public SecurityConfig(JWTTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/shifts/**").hasRole("EMPLOYER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

@Bean
public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the "ROLE_" prefix if necessary
}

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
