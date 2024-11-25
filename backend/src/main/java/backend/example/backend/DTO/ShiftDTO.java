package backend.example.backend.DTO;

import lombok.Data;

import java.util.List;



@Data
public class ShiftDTO {
    private String id;
    private String date;
    private String startTime;
    private String endTime;
    private int maxEmployees;
    private List<String> requiredRoles;
    private List<EmployeeDTO> assignedEmployees;

    public ShiftDTO(String id, String date, String startTime, String endTime, int maxEmployees, List<String> requiredRoles, List<EmployeeDTO> assignedEmployees) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxEmployees = maxEmployees;
        this.requiredRoles = requiredRoles;
        this.assignedEmployees = assignedEmployees;
    }

  
}