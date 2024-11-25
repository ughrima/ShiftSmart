package backend.example.backend.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.*;

// //Employees data
// @Data
// @Document(collection = "employees")
// public class Employee {
//     @Id
//     private String id;

//     private String name;
//     private String email;
//     private String role;

//     private int currentWorkHours = 0; // Default value for work hours
//     private int totalWorkHoursThisWeek = 0; // Default value for total work hours
//     private List<String> availability = new ArrayList<>(); // Default as an empty list
//     private List<Shift> assignedShifts = new ArrayList<>(); // Default as an empty list
    
//     @DBRef
//     private Employer employer;
// }

@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    private String name;
    private String email;
    private String role;
    private int currentWorkHours = 0; // Default value for work hours
    private int totalWorkHoursThisWeek = 0; // Default value for total work hours
    private List<String> availability = new ArrayList<>(); // Default as an empty list
    private List<Shift> assignedShifts = new ArrayList<>(); // Default as an empty list
    
    @DBRef
    private Employer employer;

    private int maxWorkHoursPerDay = 8;  // For example, setting a max limit of 8 hours per day
    private int maxWorkHoursPerWeek = 40;  // For example, setting a max limit of 40 hours per week

    // Getter for maxWorkHoursPerDay
    public int getMaxWorkHoursPerDay() {
        return this.maxWorkHoursPerDay;
    }

    // Getter for maxWorkHoursPerWeek
    public int getMaxWorkHoursPerWeek() {
        return this.maxWorkHoursPerWeek;
    }
}
