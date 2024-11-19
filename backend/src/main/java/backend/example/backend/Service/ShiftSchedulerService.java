package backend.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.example.backend.Entity.*;
import backend.example.backend.Repository.EmployeeRepository;
import backend.example.backend.Repository.ShiftRepository;
import java.util.*;

@Service
public class ShiftSchedulerService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ConflictGraph conflictGraph;

    public Map<Shift, List<Shift>> getConflicts() {
        // Return all conflicts from the ConflictGraph
        return conflictGraph.getAllConflicts();
    }

    private PriorityQueue<Employee> initializePriorityQueue(List<Employee> employees) {
        PriorityQueue<Employee> queue = new PriorityQueue<>(
            Comparator.comparingInt(Employee::getTotalWorkHoursThisWeek)
                .thenComparingInt(Employee::getCurrentWorkHours)
        );
        queue.addAll(employees);
        return queue;
    }
    public void assignShifts(List<Shift> shifts, Employer employer) {
        // Validate inputs
        if (shifts == null || shifts.isEmpty()) {
            throw new IllegalArgumentException("Shifts list cannot be null or empty");
        }
        if (employer == null) {
            throw new IllegalArgumentException("Employer cannot be null");
        }
    
        System.out.println("Assigning shifts for employer: " + employer.getId());
        System.out.println("Total shifts to schedule: " + shifts.size());
    
        List<Employee> employees = employeeRepository.findByEmployerId(employer.getId());
        if (employees == null || employees.isEmpty()) {
            throw new IllegalArgumentException("No employees available for scheduling");
        }
    
        System.out.println("Total employees available: " + employees.size());
    
        PriorityQueue<Employee> availableEmployees = initializePriorityQueue(employees);
    
        for (Shift shift : shifts) {
            System.out.println("Scheduling shift: " + shift.getId());
            boolean assigned = false;
    
            for (Employee employee : availableEmployees) {
                // Check max employees per shift
                if (shift.getAssignedEmployees().size() >= shift.getMaxEmployees()) {
                    System.out.println("Shift " + shift.getId() + " already has maximum assigned employees.");
                    break;
                }
    
                if (isAvailable(employee, shift) && canWork(employee, employer, shift)) {
                    shift.getAssignedEmployees().add(employee);
                    employee.setCurrentWorkHours(employee.getCurrentWorkHours() + shift.getDuration());
                    employee.setTotalWorkHoursThisWeek(employee.getTotalWorkHoursThisWeek() + shift.getDuration());
                    employee.getAssignedShifts().add(shift);
    
                    // Save the updated employee and shift
                    shiftRepository.save(shift);
                    employeeRepository.save(employee);
    
                    // Update the priority queue
                    availableEmployees.remove(employee);
                    availableEmployees.add(employee);
    
                    System.out.println("Assigned employee " + employee.getName() + " to shift: " + shift.getId());
                    assigned = true;
                    break;
                } else {
                    if (!isAvailable(employee, shift)) {
                        System.out.println("Employee " + employee.getName() + " is not available for shift: " + shift.getId());
                    }
                    if (!canWork(employee, employer, shift)) {
                        System.out.println("Employee " + employee.getName() + " cannot work shift " + shift.getId() + " due to work hour or role constraints.");
                    }
                }
            }
    
            // If not assigned, add to conflict graph
            if (!assigned) {
                System.out.println("Shift " + shift.getId() + " could not be assigned. Adding conflicts.");
                for (Shift otherShift : shifts) {
                    if (hasConflict(shift, otherShift)) {
                        conflictGraph.addEdge(shift, otherShift);
                        System.out.println("Conflict detected between shift " + shift.getId() + " and shift " + otherShift.getId());
                    }
                }
            }
        }
    
        resolveConflicts(shifts, employer);
    }
    

    public void resolveConflicts(List<Shift> shifts, Employer employer) {
        Map<Shift, Employee> assignedShifts = new HashMap<>();
        int maxAttempts = shifts.size(); // Set a limit based on the number of shifts
    
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            System.out.println("Attempting to resolve conflicts. Iteration: " + (attempt + 1));
            boolean progressMade = false;
    
            for (Shift shift : shifts) {
                if (findMatching(shift, assignedShifts, new HashSet<>())) {
                    progressMade = true;
                    System.out.println("Progress made in conflict resolution during iteration: " + (attempt + 1));
                }
            }
    
            // Break early if no progress is made in this iteration
            if (!progressMade) {
                System.out.println("No further progress made in conflict resolution. Breaking loop.");
                break;
            }
        }
    
        // Log unresolved conflicts for manual intervention
        if (!conflictGraph.getAllConflicts().isEmpty()) {
            System.out.println("Unresolved conflicts remain after " + maxAttempts + " resolution attempts.");
            System.out.println("Conflicting shifts: " + conflictGraph.getAllConflicts());
        }
    }
    
    

private boolean findMatching(Shift shift, Map<Shift, Employee> assignedShifts, Set<Employee> visited) {
    for (Employee employee : shift.getAssignedEmployees()) {
        if (!visited.contains(employee)) {
            visited.add(employee);

            // Check role compatibility
            if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().isEmpty() &&
                !shift.getRequiredRoles().contains(employee.getRole())) {
                continue; // Skip employees with non-matching roles
            }

            for (Shift conflictingShift : conflictGraph.getConflicts(shift)) {
                if (!assignedShifts.containsKey(conflictingShift) || findMatching(conflictingShift, assignedShifts, visited)) {
                    assignedShifts.put(shift, employee);
                    return true;
                }
            }
        }
    }
    System.out.println("No match found for shift: " + shift.getId());
    return false;
}

private boolean isAvailable(Employee employee, Shift shift) {
    // Check date availability
    if (employee.getAvailability() == null || !employee.getAvailability().contains(shift.getDate())) {
        System.out.println("Employee " + employee.getName() + " is not available on date: " + shift.getDate());
        return false;
    }

    // Check role compatibility
    if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().isEmpty() &&
        !shift.getRequiredRoles().contains(employee.getRole())) {
        System.out.println("Employee " + employee.getName() + " has a role mismatch for shift: " + shift.getId());
        return false; // Role mismatch
    }

    // Check time overlap with already assigned shifts
    for (Shift assignedShift : employee.getAssignedShifts()) {
        boolean overlaps = shift.getStartTime().compareTo(assignedShift.getEndTime()) < 0 &&
                           shift.getEndTime().compareTo(assignedShift.getStartTime()) > 0;
        if (overlaps && !employee.getEmployer().isFlexibleWorkHours()) {
            System.out.println("Employee " + employee.getName() + " has overlapping shifts and cannot take shift: " + shift.getId());
            return false; // Overlapping shift not allowed without flexible work hours
        }
    }
    return true;
}


private boolean canWork(Employee employee, Employer employer, Shift shift) {
    if (shift.isNightShift() && !employer.isAllowNightShifts()) {
        return false;
    }
    
    if (employer.isFlexibleWorkHours()) {
        // Calculate total distinct hours including this shift
        List<Shift> allShifts = new ArrayList<>(employee.getAssignedShifts());
        allShifts.add(shift);

        int totalDistinctHours = calculateDistinctHours(allShifts);
        return totalDistinctHours <= employer.getMaxShiftsPerWeek();
    }

    // Standard daily and weekly hour limits
    return (employee.getCurrentWorkHours() + shift.getDuration() <= employer.getMaxWorkHoursPerDay())
        && (employee.getTotalWorkHoursThisWeek() + shift.getDuration() <= employer.getMaxShiftsPerWeek());
}


private boolean hasConflict(Shift shift1, Shift shift2) {
    for (Employee employee : shift1.getAssignedEmployees()) {
        if (shift2.getAssignedEmployees().contains(employee)) {
            return true;
        }
    }
    return false;
}

private int calculateDistinctHours(List<Shift> shifts) {
    if (shifts.isEmpty()) {
        return 0;
    }

    // Convert shifts to time intervals
    List<int[]> intervals = new ArrayList<>();
    for (Shift shift : shifts) {
        intervals.add(new int[] {
            Integer.parseInt(shift.getStartTime().replace(":", "")), // e.g., "09:00" -> 900
            Integer.parseInt(shift.getEndTime().replace(":", ""))    // e.g., "17:00" -> 1700
        });
    }

    // Sort intervals by start time
    intervals.sort(Comparator.comparingInt(a -> a[0]));

    // Merge intervals
    int totalHours = 0;
    int[] currentInterval = intervals.get(0);
    for (int i = 1; i < intervals.size(); i++) {
        int[] nextInterval = intervals.get(i);

        // Overlapping intervals
        if (currentInterval[1] >= nextInterval[0]) {
            currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
        } else {
            // Non-overlapping interval
            totalHours += (currentInterval[1] - currentInterval[0]) / 100; // Add current interval duration
            currentInterval = nextInterval;
        }
    }

    // Add the last interval
    totalHours += (currentInterval[1] - currentInterval[0]) / 100;
    return totalHours;
}

}
