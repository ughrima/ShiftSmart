package backend.example.backend.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the employer by email
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new UsernameNotFoundException("Employer not found with email: " + email);
        }

        // Return Spring Security's User object
        return org.springframework.security.core.userdetails.User.builder()
                .username(employer.getEmail())
                .password(employer.getPassword()) // Already hashed with BCrypt
                .roles("EMPLOYER") // Assign the role
                .build();
    }
}
