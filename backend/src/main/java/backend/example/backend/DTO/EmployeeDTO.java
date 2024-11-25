package backend.example.backend.DTO;

import lombok.Data;

@Data
public class EmployeeDTO {
    private String id;
    private String name;
    private String role;

    public EmployeeDTO(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters and setters
}