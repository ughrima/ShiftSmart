package backend.example.backend.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.example.backend.Entity.Shift;
import backend.example.backend.Service.ShiftService;


@RestController
@RequestMapping("/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    //to generate shifts
    @PostMapping("/generate")
    public ResponseEntity<String> generateShifts(@RequestBody Shift request) {
        shiftService.generateShifts(request.getEmployerId(), request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok("Shifts generated successfully");
    }
}
