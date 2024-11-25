package backend.example.backend.Controller;

import backend.example.backend.DTO.ScheduleRequest;
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

import java.util.List;

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

    // // Schedule Shifts
    // @PreAuthorize("hasRole('EMPLOYER')")
    // @PostMapping("/schedule")
    // public ResponseEntity<?> scheduleShifts(@RequestBody ScheduleRequest scheduleRequest) {
    //     String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    //     Employer employer = employerRepository.findByEmail(employerEmail);

    //     // if (employer == null || !employer.getId().equals(scheduleRequest.getEmployerId())) {
    //     //     return ResponseEntity.status(403).body("Unauthorized to schedule shifts for this employer");
    //     // }
    //     if (!employer.getId().equals(scheduleRequest.getEmployerId())) {
    //         return ResponseEntity.status(403).body("Unauthorized to schedule shifts for this employer");
    //     }
        
    //     try {
    //         shiftSchedulerService.assignShifts(scheduleRequest.getShifts(), employer);
    //         return ResponseEntity.ok("Shifts scheduled successfully");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body("Error scheduling shifts: " + e.getMessage());
    //     }
    // }

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleShifts(@RequestBody ScheduleRequest scheduleRequest) {
        String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employerRepository.findByEmail(employerEmail);

        if (employer == null) {
            return ResponseEntity.status(403).body("Unauthorized: Employer not found");
        }

        // if (!employer.getId().equals(scheduleRequest.getEmployerId())) {
        //     return ResponseEntity.status(403).body("Unauthorized to schedule shifts for this employer");
        // }
    
        try {
            shiftSchedulerService.assignShifts(scheduleRequest.getShifts(), employer);
            return ResponseEntity.ok("Shifts scheduled successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
    

    // Fetch Scheduled Shifts
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/scheduled")
    public ResponseEntity<?> getScheduledShifts() {
        String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employerRepository.findByEmail(employerEmail);

        if (employer == null) {
            return ResponseEntity.status(403).body("Unauthorized: Employer not found");
        }

        try {
            List<Shift> shifts = shiftRepository.findByEmployerId(employer.getId());
            return ResponseEntity.ok(shifts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching scheduled shifts: " + e.getMessage());
        }
    }
}
