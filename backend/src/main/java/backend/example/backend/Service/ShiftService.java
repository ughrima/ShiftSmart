package backend.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.example.backend.Entity.Employee;
import backend.example.backend.Entity.Shift;
import backend.example.backend.Repository.ShiftRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class ShiftService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ShiftRepository shiftRepository;

    //generate shifts of employees
    public void generateShifts(String employerId, LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        List<Employee> employees = employeeService.getEmployeesByEmployer(employerId);

        // PQ to assign shifts based on priority
        PriorityQueue<Employee> pq = new PriorityQueue<>((a, b) -> calculatePriority(b, shiftStart) - calculatePriority(a, shiftStart));

        for (Employee emp : employees) {
            if (isEmployeeAvailable(emp, shiftStart, shiftEnd)) {
                pq.add(emp);
            }
        }

        // Assign shifts to employees from the PQ
        while (!pq.isEmpty()) {
            Employee selectedEmployee = pq.poll();
            assignShift(selectedEmployee, shiftStart, shiftEnd, employerId);
        }
    }

    // rank employees on prefs like night shifts etc
    private int calculatePriority(Employee emp, LocalDateTime shiftStart) {
        int priority = 0;
        if (emp.getPreferences().get("noNightShifts") && isNightShift(shiftStart)) {
            priority -= 10;  // Lower priority if employee prefers no night shifts
        }
        return priority;
    }

    // Check availability for the shift based on their schedule
    private boolean isEmployeeAvailable(Employee emp, LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        String dayOfWeek = shiftStart.getDayOfWeek().toString();  // Get the day of the shift (e.g., MONDAY)
        String availableHours = emp.getAvailability().get(dayOfWeek);  // Get employee's availability for the day

        if (availableHours != null) {
            String[] hours = availableHours.split("-");
            LocalDateTime availableStart = LocalDateTime.parse(shiftStart.toLocalDate() + "T" + hours[0].trim());
            LocalDateTime availableEnd = LocalDateTime.parse(shiftStart.toLocalDate() + "T" + hours[1].trim());

            return !shiftStart.isBefore(availableStart) && !shiftEnd.isAfter(availableEnd);  // Check within availability
        }
        return false;  // Employee unavailable if no working hours found
    }

    // Check if the shift is a night shift
    private boolean isNightShift(LocalDateTime shiftStart) {
        int hour = shiftStart.getHour();
        return hour >= 21 || hour < 6;  // Shift starts at or after 9 PM, or before 6 AM
    }

    // Assign the shift to the employee and save it in the repository
    private void assignShift(Employee emp, LocalDateTime shiftStart, LocalDateTime shiftEnd, String employerId) {
        Shift shift = new Shift();
        shift.setEmployeeId(emp.getId());
        shift.setStartTime(shiftStart);
        shift.setEndTime(shiftEnd);
        shift.setEmployerId(employerId);
        shiftRepository.save(shift);
    }
}
