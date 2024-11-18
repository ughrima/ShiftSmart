package backend.example.backend.Controller;

import backend.example.backend.Entity.*;
import backend.example.backend.Service.ShiftSchedulerService;
import backend.example.backend.Repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
public class ShiftController {

    @Autowired
    private ShiftSchedulerService shiftSchedulerService;

    @Autowired
    private ShiftRepository shiftRepository;

    // 1. Create Shifts
    @PostMapping("/create")
    public ResponseEntity<?> createShift(@RequestBody Shift shift) {
        try {
            Shift savedShift = shiftRepository.save(shift);
            return ResponseEntity.ok(savedShift);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating shift: " + e.getMessage());
        }
    }

    // 2. Schedule Shifts
    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleShifts(@RequestBody List<Shift> shifts, @RequestParam String employerId) {
        try {
            Employer employer = new Employer(); // Mock Employer. Replace with actual retrieval logic.
            employer.setId(employerId); // Assuming Employer ID is enough for lookup.

            shiftSchedulerService.assignShifts(shifts, employer);
            return ResponseEntity.ok("Shifts scheduled successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scheduling shifts: " + e.getMessage());
        }
    }

    // 3. Fetch Scheduled Shifts
    @GetMapping("/scheduled")
    public ResponseEntity<?> getScheduledShifts(@RequestParam String employerId) {
        try {
            List<Shift> shifts = shiftRepository.findByEmployerId(employerId);
            return ResponseEntity.ok(shifts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching scheduled shifts: " + e.getMessage());
        }
    }

    // 4. Resolve Conflicts
    @GetMapping("/conflicts")
    public ResponseEntity<?> getConflicts() {
        try {
            return ResponseEntity.ok(shiftSchedulerService.getConflicts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching conflicts: " + e.getMessage());
        }
    }

    // 5. Generate Reports (Optional)
    @GetMapping("/report")
    public ResponseEntity<?> generateReport(@RequestParam String employerId) {
        try {
            // Mock report logic. Replace with actual report generation.
            String report = "Report for employer ID: " + employerId;
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating report: " + e.getMessage());
        }
    }
}
