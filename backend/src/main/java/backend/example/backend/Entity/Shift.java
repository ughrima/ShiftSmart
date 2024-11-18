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

    @DBRef
    private Employer employer; // Associated employer
    
    @DBRef
    private List<Employee> assignedEmployees = new ArrayList<>(); // Employees assigned to this shift
}

