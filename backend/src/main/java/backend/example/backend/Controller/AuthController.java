package backend.example.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;
import backend.example.backend.Security.jwt.JWTTokenProvider;
import backend.example.backend.DTO.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployerRepository employerRepository;

    //to login
    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String token = tokenProvider.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    //to register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Employer employer = new Employer();
        employer.setEmail(registerRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));  // Hash password
        employer.setCompanyName(registerRequest.getCompanyName());
        employerRepository.save(employer);
        return ResponseEntity.ok("Employer registered successfully");
    }
}


