package backend.example.backend.Entity;
import backend.example.backend.Entity.*;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//Employers who make their accounts
@Data
@Document(collection = "employers")
public class Employer {
    @Id
    private String id;
    private String companyName;
    private String email;
    private String Password;
    private Company policy;
}
