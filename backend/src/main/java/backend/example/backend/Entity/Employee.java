package backend.example.backend.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Map;

//Employees data
@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    private String employerId;
    private String Name;
    private Map<String,String> availability;
    private Map<String,Boolean>preferences;
    private String[] skills;
    private int assignedHours;

    public Map<String, Boolean> getPreferences() {
        return preferences;
    }
    
    public void setPreferences(Map<String, Boolean> preferences) {
        this.preferences = preferences;
    }
    
}
