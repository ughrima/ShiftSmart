package backend.example.backend.DTO;

import backend.example.backend.Entity.Shift;
import java.util.List;

public class ScheduleRequest {
    private String employerId;
    private List<Shift> shifts;

    // Getters and Setters
    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
}
