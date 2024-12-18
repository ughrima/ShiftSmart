package backend.example.backend.Entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

import java.util.*;
import lombok.Data;


@Data
@Document(collection = "shifts")
public class Shift {
    @NotNull
    private String id;

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    private boolean isNightShift;
    private int duration;
    private int maxEmployees;

    private List<String> requiredRoles = new ArrayList<>();

    @DBRef
    private Employer employer;

    @DBRef(lazy = false)
private List<Employee> assignedEmployees = new ArrayList<>();

}
