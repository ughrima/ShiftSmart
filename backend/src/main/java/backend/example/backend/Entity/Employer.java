// package backend.example.backend.Entity;
// import backend.example.backend.Entity.*;
// import lombok.Data;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.mongodb.core.mapping.Document;

// //Employers who make their accounts

// @Document(collection = "employers")
// public class Employer {
//     @Id
//     private String id;
//     private String email;
//     private String password;
//     private String companyName;

//     @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL)
//     private List<Employee> employees = new ArrayList<>();
//     // Getters and setters
// }
package backend.example.backend.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.persistence.*;
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

    // One-to-many relationship with employees
    @DBRef
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();
}
