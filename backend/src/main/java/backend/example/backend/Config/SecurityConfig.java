// package backend.example.backend.Config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import backend.example.backend.Security.jwt.JWTTokenFilter;


// @Configuration
// @EnableWebSecurity


// //configures Spring Security to control Authentication and Authorization
// public class SecurityConfig  {

//     @Autowired

//     // for the endpoints
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTTokenFilter jwtTokenFilter) throws Exception {
//         http.csrf().disable()
//             .authorizeHttpRequests((authz) -> authz
//                 .requestMatchers("/auth/**").permitAll()  // Publicly accessible routes
//                 .anyRequest().authenticated()  // Secured routes
//             )
//             .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
//         return http.build();
//     }

    
//     //hash password
//     @Bean
//     public PasswordEncoder passwordEncoder(){
//         return new BCryptPasswordEncoder();
//     }

//     //handle authentication reqs
//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }
// }
package backend.example.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import backend.example.backend.Security.jwt.JWTTokenFilter;

@Configuration
@EnableWebSecurity
// Configures Spring Security to control Authentication and Authorization
public class SecurityConfig {

    private final JWTTokenFilter jwtTokenFilter;

    // Constructor injection for JWTTokenFilter
    public SecurityConfig(JWTTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    // Configuring the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll()  // Publicly accessible routes
                .anyRequest().authenticated()  // Secured routes
            )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean for password encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for handling authentication requests
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
