package backend.example.backend.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;
import lombok.Data;

@Data
@Document(collection = "shifts")
public class Shift {
    @Id
    private String id;

    private String date; // "YYYY-MM-DD"
    private String startTime; // e.g., "09:00"
    private String endTime; // e.g., "17:00"
    private boolean isNightShift; //yes or a no
    private int duration; // Calculated duration in hours
    private int maxEmployees; //max employees per that shift

    @DBRef
    private Employer employer; // Associated employer
    
    private List<String> requiredRoles = new ArrayList<>(); 
    
    @DBRef
    private List<Employee> assignedEmployees = new ArrayList<>(); // Employees assigned to this shift
}

