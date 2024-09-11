package backend.example.backend.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

//policies of each company
@Data
@Document(collection = "policies")
public class Company {
    @Id
    private String id;
    private int maxHoursPerWeek;
    private boolean allowNightShifts;
    private boolean weekendOff;
}

