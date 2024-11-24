
package backend.example.backend.Controller;

import backend.example.backend.DTO.*;
import backend.example.backend.Entity.*;
import backend.example.backend.Repository.*;
import backend.example.backend.Security.jwt.JWTTokenProvider;
import backend.example.backend.Service.CustomUserDetailsService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/auth")
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

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate tokens
            String accessToken = tokenProvider.generateToken(userDetails);
            String refreshToken = tokenProvider.generateRefreshToken(userDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }

    // Register Employer API
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
            employer.setWorkDaysPerWeek(registerRequest.getWorkDaysPerWeek());
            employer.setAllowNightShifts(registerRequest.isAllowNightShifts());
            employer.setMaxWorkHoursPerDay(registerRequest.getMaxWorkHoursPerDay());
            employer.setMandatoryWeekendsOff(registerRequest.isMandatoryWeekendsOff());
            employer.setFlexibleWorkHours(registerRequest.isFlexibleWorkHours());
            employer.setRoles(new HashSet<>(Collections.singletonList("ROLE_EMPLOYER")));

            MultipartFile employeeFile = registerRequest.getEmployeeExcel();
            List<Employee> employees = parseCsv(employeeFile, employer);

            Employer savedEmployer = employerRepository.save(employer);
            employees.forEach(employee -> employee.setEmployer(savedEmployer));
            employeeRepository.saveAll(employees);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Employer registered successfully");
            response.put("employerId", savedEmployer.getId());
            response.put("companyName", savedEmployer.getCompanyName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during registration: " + e.getMessage());
        }
    }

    // Helper method: Parse CSV
    private List<Employee> parseCsv(MultipartFile file, Employer employer) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is empty or not provided.");
        }

        List<Employee> employees = new ArrayList<>();
        Set<String> validDays = Set.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

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
                    employee.setEmployer(employer);

                    String availability = record.get("Availability");
                    if (availability != null && !availability.isEmpty()) {
                        List<String> items = Arrays.asList(availability.split(","));
                        boolean validItems = items.stream()
                                .allMatch(item -> validDays.contains(item) || isValidDate(item));
                        if (validItems) {
                            employee.setAvailability(items);
                        } else {
                            throw new IllegalArgumentException("Invalid day or date in Availability: " + items);
                        }
                    } else {
                        employee.setAvailability(new ArrayList<>());
                    }

                    employee.setCurrentWorkHours(safeParseInt(record.get("CurrentWorkHours"), 0));
                    employee.setTotalWorkHoursThisWeek(safeParseInt(record.get("TotalWorkHoursThisWeek"), 0));

                    employees.add(employee);
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
            return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
