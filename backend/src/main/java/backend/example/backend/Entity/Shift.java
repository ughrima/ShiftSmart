package backend.example.backend.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "shifts")
public class Shift {
    @Id
    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String employeeid;
    private String employerId;

}
