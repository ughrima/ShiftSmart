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
    // Fetch employees and initialize Priority Queue
    List<Employee> employees = employeeRepository.findByEmployerId(employer.getId());
    PriorityQueue<Employee> availableEmployees = initializePriorityQueue(employees);

    for (Shift shift : shifts) {
        boolean assigned = false;

        for (Employee employee : availableEmployees) {
            if (isAvailable(employee, shift) && canWork(employee, employer, shift)) {
                // Assign employee to shift
                shift.getAssignedEmployees().add(employee);
                employee.setCurrentWorkHours(employee.getCurrentWorkHours() + shift.getDuration());
                employee.setTotalWorkHoursThisWeek(employee.getTotalWorkHoursThisWeek() + shift.getDuration());
                employee.getAssignedShifts().add(shift);

                // Save the updated employee and shift
                shiftRepository.save(shift);
                employeeRepository.save(employee);

                assigned = true;
                break;
            }
        }

        // If not assigned, add conflict
        if (!assigned) {
            for (Shift otherShift : shifts) {
                if (hasConflict(shift, otherShift)) {
                    conflictGraph.addEdge(shift, otherShift);
                }
            }
        }
    }

    resolveConflicts(shifts, employer);
}
public void resolveConflicts(List<Shift> shifts, Employer employer) {
    Map<Shift, Employee> assignedShifts = new HashMap<>();

    for (Shift shift : shifts) {
        findMatching(shift, assignedShifts, new HashSet<>());
    }
}

private boolean findMatching(Shift shift, Map<Shift, Employee> assignedShifts, Set<Employee> visited) {
    System.out.println("Finding match for shift: " + shift.getId());

    for (Employee employee : shift.getAssignedEmployees()) {
        System.out.println("Checking employee: " + employee.getId());

        if (!visited.contains(employee)) {
            visited.add(employee);

            for (Shift conflictingShift : conflictGraph.getConflicts(shift)) {
                System.out.println("Conflicting shift: " + conflictingShift.getId());

                if (!assignedShifts.containsKey(conflictingShift) || findMatching(conflictingShift, assignedShifts, visited)) {
                    assignedShifts.put(shift, employee);
                    System.out.println("Assigned employee " + employee.getId() + " to shift " + shift.getId());
                    return true;
                }
            }
        }
    }
    System.out.println("No match found for shift: " + shift.getId());
    return false;
}

private boolean isAvailable(Employee employee, Shift shift) {
    return employee.getAvailability() != null && employee.getAvailability().contains(shift.getDate());
}
private boolean canWork(Employee employee, Employer employer, Shift shift) {
    // Check if the employee exceeds max work hours per day or week
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

}
