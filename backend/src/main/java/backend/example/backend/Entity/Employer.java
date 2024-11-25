
package backend.example.backend.Entity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @DBRef
    private List<Shift> assignedShifts = new ArrayList<>();


    // Reference to a list of employees
    @DBRef
    private List<Employee> employees = new ArrayList<>();

    
}
