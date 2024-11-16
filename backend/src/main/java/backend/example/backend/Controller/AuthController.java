// package backend.example.backend.Controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import backend.example.backend.Entity.Employee;
// import backend.example.backend.Entity.Employer;
// import backend.example.backend.Repository.EmployerRepository;
// import backend.example.backend.Security.jwt.JWTTokenProvider;
// import backend.example.backend.DTO.*;
// import org.springframework.security.core.Authentication;

// @RestController
// @RequestMapping("auth")
// public class AuthController {
    
//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private JWTTokenProvider tokenProvider;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Autowired
//     private EmployerRepository employerRepository;

//     //to login
//     @PostMapping("/login")
//         public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//         Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//         );
//         String token = tokenProvider.generateToken(loginRequest.getEmail());
//         return ResponseEntity.ok(new JWTAuthResponse(token));
//     }

// //     //to register
// //     @PostMapping("/register")
// //     public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
// //         Employer employer = new Employer();
// //         employer.setEmail(registerRequest.getEmail());
// //         employer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));  // Hash password
// //         employer.setCompanyName(registerRequest.getCompanyName());
// //         employerRepository.save(employer);
// //         return ResponseEntity.ok("Employer registered successfully");
// //     }
// // }


// @PostMapping("/register")
// public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
//     Employer employer = new Employer();
//     employer.setEmail(registerRequest.getEmail());
//     employer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));  // Hash password
//     employer.setCompanyName(registerRequest.getCompanyName());

//     // Parse CSV and add employees
//     MultipartFile employeeFile = registerRequest.getEmployeeExcel();
//     List<Employee> employees = parseCsv(employeeFile);
//     employees.forEach(employee -> employee.setEmployer(employer)); // Link employees to employer
    
//     employerRepository.save(employer);
//     employeeRepository.saveAll(employees); // Save employees

//     return ResponseEntity.ok("Employer registered successfully");


// private List<Employee> parseCsv(MultipartFile file) {
//     List<Employee> employees = new ArrayList<>();
//     try (Reader reader = new InputStreamReader(file.getInputStream())) {
//         Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
//         for (CSVRecord record : records) {
//             Employee employee = new Employee();
//             employee.setName(record.get("Name"));
//             employee.setEmail(record.get("Email"));
//             employee.setRole(record.get("Role"));
//             // Add other fields as necessary
//             employees.add(employee);
//         }
//     } catch (IOException e) {
//         throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
//     }
//     return employees;
// }

// }
// }

package backend.example.backend.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import backend.example.backend.Entity.Employee;
import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployeeRepository;
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

    @Autowired
    private EmployeeRepository employeeRepository; // Ensure this is defined

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String token = tokenProvider.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterRequest registerRequest) {
        Employer employer = new Employer();
        employer.setEmail(registerRequest.getEmail());
        employer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));  // Hash password
        employer.setCompanyName(registerRequest.getCompanyName());

        // Parse CSV and add employees
        MultipartFile employeeFile = registerRequest.getEmployeeExcel();
        List<Employee> employees = parseCsv(employeeFile);
        employees.forEach(employee -> employee.setEmployer(employer)); // Link employees to employer

        employerRepository.save(employer);
        employeeRepository.saveAll(employees); // Save employees

        return ResponseEntity.ok("Employer registered successfully");
    }

    // CSV Parsing Logic
    private List<Employee> parseCsv(MultipartFile file) {
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : records) {
                Employee employee = new Employee();
                employee.setName(record.get("Name"));
                employee.setEmail(record.get("Email"));
                employee.setRole(record.get("Role"));
                // Add other fields as necessary
                employees.add(employee);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return employees;
    }
}
