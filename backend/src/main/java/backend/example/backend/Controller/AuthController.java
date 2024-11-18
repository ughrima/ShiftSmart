package backend.example.backend.Controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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

import java.util.*
;
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
    private EmployeeRepository employeeRepository; 


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
        if (employerRepository.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Employer with this email already exists!");
        }
    
        try {
            Employer employer = new Employer();
            employer.setEmail(registerRequest.getEmail());
            employer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            employer.setCompanyName(registerRequest.getCompanyName());

            //policies
            employer.setWorkDaysPerWeek(registerRequest.getWorkDaysPerWeek());
            employer.setAllowNightShifts(registerRequest.isAllowNightShifts());
            employer.setMaxWorkHoursPerDay(registerRequest.getMaxWorkHoursPerDay());
            employer.setMandatoryWeekendsOff(registerRequest.isMandatoryWeekendsOff());
            employer.setFlexibleWorkHours(registerRequest.isFlexibleWorkHours());
    
            MultipartFile employeeFile = registerRequest.getEmployeeExcel();
            List<Employee> employees = parseCsv(employeeFile);
            employees.forEach(employee -> employee.setEmployer(employer));
    
            employerRepository.save(employer);
            System.out.println("Saved Employees: " + employees);

            employeeRepository.saveAll(employees);
    
            return ResponseEntity.ok("Employer registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during registration: " + e.getMessage());
        }
    }

    //parsing csv logic
    private List<Employee> parseCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("CSV file is empty or not provided.");
        }
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader("Name", "Email", "Role", "Availability", "CurrentWorkHours", "TotalWorkHoursThisWeek")
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
    
            for (CSVRecord record : records) {
                try {
                    Employee employee = new Employee();
                    employee.setName(record.get("Name"));
                    employee.setEmail(record.get("Email"));
                    employee.setRole(record.get("Role"));
    
                    String availability = record.get("Availability");
                    employee.setAvailability(availability != null && !availability.isEmpty()
                        ? Arrays.asList(availability.split(","))
                        : new ArrayList<>());
    
                    employee.setCurrentWorkHours(safeParseInt(record.get("CurrentWorkHours"), 0));
                    employee.setTotalWorkHoursThisWeek(safeParseInt(record.get("TotalWorkHoursThisWeek"), 0));
    
                    employees.add(employee);
                    System.out.println("Successfully parsed record: " + employee);
                } catch (Exception e) {
                    System.err.println("Failed to parse record: " + record.toString() + " Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return employees;
    }
    
    private int safeParseInt(String value, int defaultValue) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    
}
