package backend.example.backend.Controller;

import backend.example.backend.DTO.EmployeeDTO;
import backend.example.backend.DTO.ScheduleRequest;
import backend.example.backend.DTO.ShiftDTO;
import backend.example.backend.Entity.Employer;
import backend.example.backend.Entity.Shift;
import backend.example.backend.Repository.EmployerRepository;
import backend.example.backend.Repository.ShiftRepository;
import backend.example.backend.Service.ShiftSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    @Autowired
    private ShiftSchedulerService shiftSchedulerService;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private EmployerRepository employerRepository;

    // Create Shifts
    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping("/create")
    public ResponseEntity<?> createShift(@RequestBody Shift shift) {
        String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employerRepository.findByEmail(employerEmail);

        if (employer == null) {
            return ResponseEntity.status(403).body("Unauthorized: Employer not found");
        }

        try {
            shift.setEmployer(employer);
            Shift savedShift = shiftRepository.save(shift);
            return ResponseEntity.ok(savedShift);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating shift: " + e.getMessage());
        }
    }

    @PostMapping("/schedule")
public ResponseEntity<?> scheduleShifts(@RequestBody ScheduleRequest scheduleRequest) {
    String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    Employer employer = employerRepository.findByEmail(employerEmail);

    if (employer == null) {
        return ResponseEntity.status(403).body("Unauthorized: Employer not found");
    }

    try {
        // Ensure shifts are available in the request
        List<Shift> shiftsToAssign = scheduleRequest.getShifts();
        if (shiftsToAssign == null || shiftsToAssign.isEmpty()) {
            return ResponseEntity.status(400).body("No shifts to assign.");
        }

        System.out.println("Shifts to be assigned: " + shiftsToAssign.size());

        // Call to service to assign shifts
        shiftSchedulerService.assignShifts(shiftsToAssign, employer);
        
        return ResponseEntity.ok("Shifts assigned successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
    }
}

    
@GetMapping("/scheduled")
public ResponseEntity<?> getScheduledShifts() {
    String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    Employer employer = employerRepository.findByEmail(employerEmail);

    if (employer == null) {
        return ResponseEntity.status(403).body("Unauthorized: Employer not found");
    }

    try {
        List<Shift> shifts = shiftRepository.findByEmployerId(employer.getId());
        List<ShiftDTO> response = shifts.stream().map(shift -> {
            // Map each shift to ShiftDTO
            List<EmployeeDTO> assignedEmployeeDTOs = shift.getAssignedEmployees().stream()
                .map(employee -> new EmployeeDTO(employee.getId(), employee.getName(), employee.getRole()))
                .collect(Collectors.toList());

            return new ShiftDTO(shift.getId(), shift.getDate(), shift.getStartTime(), shift.getEndTime(),
                                shift.getMaxEmployees(), shift.getRequiredRoles(), assignedEmployeeDTOs);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error fetching scheduled shifts: " + e.getMessage());
    }
}


}

