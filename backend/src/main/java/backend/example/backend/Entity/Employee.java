package backend.example.backend.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;



//Employees data
@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;

    private String name;
    private String email;
    private String role;

    @DBRef
    private Employer employer;


}
