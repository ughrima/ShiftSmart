package backend.example.backend.Security;

import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            throw new UsernameNotFoundException("Employer not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(employer.getEmail())
                .password(employer.getPassword()) // Password comes from the database
                .authorities("ROLE_EMPLOYER") // Give a role to the user
                .build();
    }
}
