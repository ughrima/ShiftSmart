// package backend.example.backend.Service;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import java.util.Collection;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;

// import backend.example.backend.Entity.Employer;
// import backend.example.backend.Repository.EmployerRepository;
// import org.springframework.security.core.userdetails.User; 

// @Service
// public class CustomUserDetailsService implements UserDetailsService {

//     @Autowired
//     private EmployerRepository employerRepository;

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         // Find the employer by email
//         Employer employer = employerRepository.findByEmail(email);
//         if (employer == null) {
//             throw new UsernameNotFoundException("Employer not found with email: " + email);
//         }

//         // Return Spring Security's User object
//         return org.springframework.security.core.userdetails.User.builder()
//                 .username(employer.getEmail())
//                 .password(employer.getPassword()) // Already hashed with BCrypt
//                 .roles("EMPLOYER") // Assign the role
//                 .build();
//     }
   


// }
package backend.example.backend.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;  // Add this import for List
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employer employer = employerRepository.findByEmail(username); // Adjust according to your domain model
        if (employer == null) {
            throw new UsernameNotFoundException("Employer not found with email: " + username);
        }
        return new User(employer.getEmail(), employer.getPassword(), getAuthorities(employer));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Employer employer) {
        if (employer.getRoles() == null || employer.getRoles().isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    
        return employer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
    
    
    
}
