// package backend.example.backend.Controller;

// import backend.example.backend.DTO.ScheduleRequest;
// import backend.example.backend.Entity.*;
// import backend.example.backend.Service.ShiftSchedulerService;
// import backend.example.backend.Repository.EmployerRepository;
// import backend.example.backend.Repository.ShiftRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/shifts")
// public class ShiftController {

//     @Autowired
//     private ShiftSchedulerService shiftSchedulerService;

//     @Autowired
//     private ShiftRepository shiftRepository;

//     @Autowired
//     private EmployerRepository employerRepository;

//     // 1. Create Shifts
//     @PostMapping("/create")
//     public ResponseEntity<?> createShift(@RequestBody Shift shift) {
//         try {
//             Shift savedShift = shiftRepository.save(shift);
//             return ResponseEntity.ok(savedShift);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Error creating shift: " + e.getMessage());
//         }
//     }


// @PostMapping("/schedule")
// public ResponseEntity<?> scheduleShifts(@RequestBody ScheduleRequest scheduleRequest) {
//     List<Shift> shifts = scheduleRequest.getShifts();
//     String employerId = scheduleRequest.getEmployerId();

//     Employer employer = employerRepository.findById(employerId)
//         .orElseThrow(() -> new IllegalArgumentException("Employer with ID " + employerId + " not found"));

//     shiftSchedulerService.assignShifts(shifts, employer);

//     return ResponseEntity.ok("Shifts scheduled successfully");
// }


//     // 3. Fetch Scheduled Shifts
//     @GetMapping("/scheduled")
//     public ResponseEntity<?> getScheduledShifts(@RequestParam String employerId) {
//         try {
//             List<Shift> shifts = shiftRepository.findByEmployerId(employerId);
//             return ResponseEntity.ok(shifts);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Error fetching scheduled shifts: " + e.getMessage());
//         }
//     }

//     // 4. Resolve Conflicts
//     @GetMapping("/conflicts")
//     public ResponseEntity<?> getConflicts() {
//         try {
//             return ResponseEntity.ok(shiftSchedulerService.getConflicts());
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Error fetching conflicts: " + e.getMessage());
//         }
//     }

//     // 5. Generate Reports (Optional)
//     @GetMapping("/report")
//     public ResponseEntity<?> generateReport(@RequestParam String employerId) {
//         try {
//             // Mock report logic. Replace with actual report generation.
//             String report = "Report for employer ID: " + employerId;
//             return ResponseEntity.ok(report);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body("Error generating report: " + e.getMessage());
//         }
//     }

    
// }

package backend.example.backend.Controller;

import backend.example.backend.DTO.ScheduleRequest;
import backend.example.backend.Entity.*;
import backend.example.backend.Service.ShiftSchedulerService;
import backend.example.backend.Repository.*;
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

        shift.setEmployer(employer);
        Shift savedShift = shiftRepository.save(shift);
        return ResponseEntity.ok(savedShift);
    }

    // Schedule Shifts
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleShifts(@RequestBody ScheduleRequest scheduleRequest) {
        String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employerRepository.findByEmail(employerEmail);

        if (employer == null || !employer.getId().equals(scheduleRequest.getEmployerId())) {
            return ResponseEntity.status(403).body("Unauthorized to schedule shifts for this employer");
        }

        shiftSchedulerService.assignShifts(scheduleRequest.getShifts(), employer);
        return ResponseEntity.ok("Shifts scheduled successfully");
    }

    // Fetch Scheduled Shifts
    @GetMapping("/scheduled")
    public ResponseEntity<?> getScheduledShifts() {
        String employerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employer employer = employerRepository.findByEmail(employerEmail);

        if (employer == null) {
            return ResponseEntity.status(403).body("Unauthorized: Employer not found");
        }

        List<Shift> shifts = shiftRepository.findByEmployerId(employer.getId());
        return ResponseEntity.ok(shifts);
    }
}
