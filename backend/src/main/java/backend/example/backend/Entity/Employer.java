package backend.example.backend.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

// Employers who make their accounts
@Data
@Document(collection = "employers")
public class Employer {
    @Id
    private String id;                // Unique identifier for the employer
    private String email;             // Employer's email address
    private String password;          // Encrypted password
    private String companyName;       // Name of the company

    private int workDaysPerWeek;
    private boolean allowNightShifts;
    private int maxWorkHoursPerDay;
    private boolean mandatoryWeekendsOff;
    private boolean flexibleWorkHours;
    private int maxShiftsPerWeek;


    // Reference to a list of employees
    @DBRef
    private List<Employee> employees = new ArrayList<>();
}
