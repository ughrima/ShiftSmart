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

    // Method to retrieve conflicts
    public Map<Shift, List<Shift>> getConflicts() {
        return conflictGraph.getAllConflicts();
    }

    // Initialize a priority queue for employees
    private PriorityQueue<Employee> initializePriorityQueue(List<Employee> employees) {
        PriorityQueue<Employee> queue = new PriorityQueue<>(
            Comparator.comparingInt(Employee::getTotalWorkHoursThisWeek)
                .thenComparingInt(Employee::getCurrentWorkHours)
        );
        queue.addAll(employees);
        return queue;
    }

    // Main method to assign shifts
    public void assignShifts(List<Shift> shifts, Employer employer) {
        if (shifts == null || shifts.isEmpty()) {
            throw new IllegalArgumentException("Shifts list cannot be null or empty.");
        }
        if (employer == null) {
            throw new IllegalArgumentException("Employer cannot be null.");
        }

        System.out.println("Assigning shifts for employer: " + employer.getId());
        System.out.println("Total shifts to schedule: " + shifts.size());

        List<Employee> employees = employeeRepository.findByEmployerId(employer.getId());
        if (employees == null || employees.isEmpty()) {
            throw new IllegalArgumentException("No employees available for scheduling.");
        }
        System.out.println("Total employees available: " + employees.size());

        PriorityQueue<Employee> availableEmployees = initializePriorityQueue(employees);

        for (Shift shift : shifts) {
            shift.setEmployer(employer); // Ensure the employer is set
            System.out.println("Processing shift: " + shift.getId());

            if (shift.getAssignedEmployees() == null) {
                shift.setAssignedEmployees(new ArrayList<>());
            }

            if (shift.getAssignedEmployees().size() >= shift.getMaxEmployees()) {
                System.out.println("Shift " + shift.getId() + " already has maximum assigned employees.");
                continue;
            }

            validateShift(shift);

            boolean assigned = false;
            for (Employee employee : availableEmployees) {
                if (shift.getAssignedEmployees().size() >= shift.getMaxEmployees()) {
                    break;
                }

                if (isAvailable(employee, shift) && canWork(employee, employer, shift)) {
                    assignEmployeeToShift(employee, shift);
                    availableEmployees.remove(employee);
                    availableEmployees.add(employee);
                    System.out.println("Assigned employee " + employee.getName() + " to shift: " + shift.getId());
                    assigned = true;
                    break;
                } else {
                    logEmployeeSkipReason(employee, shift);
                }
            }

            if (!assigned) {
                System.out.println("Shift " + shift.getId() + " could not be assigned to any employee.");
                addShiftConflicts(shift, shifts);
            }
        }

        resolveConflicts(shifts, employer);
    }

    private void validateShift(Shift shift) {
        if (shift == null || shift.getId() == null || shift.getDate() == null || 
            shift.getStartTime() == null || shift.getEndTime() == null) {
            throw new IllegalArgumentException("Each shift must have a valid ID, date, start time, and end time.");
        }
    }

    private void assignEmployeeToShift(Employee employee, Shift shift) {
        shift.getAssignedEmployees().add(employee);
        employee.setCurrentWorkHours(employee.getCurrentWorkHours() + shift.getDuration());
        employee.setTotalWorkHoursThisWeek(employee.getTotalWorkHoursThisWeek() + shift.getDuration());
        employee.getAssignedShifts().add(shift);

        shiftRepository.save(shift);
        employeeRepository.save(employee);
    }

    private void logEmployeeSkipReason(Employee employee, Shift shift) {
        if (!isAvailable(employee, shift)) {
            System.out.println("Employee " + employee.getName() + " is not available for shift: " + shift.getId());
        }
        if (!canWork(employee, shift.getEmployer(), shift)) {
            System.out.println("Employee " + employee.getName() + " cannot work shift " + shift.getId() + 
                " due to work hour or role constraints.");
        }
    }

    private void addShiftConflicts(Shift shift, List<Shift> shifts) {
        for (Shift otherShift : shifts) {
            if (hasConflict(shift, otherShift)) {
                conflictGraph.addEdge(shift, otherShift);
                System.out.println("Conflict detected between shift " + shift.getId() + " and shift " + otherShift.getId());
            }
        }
    }

    public void resolveConflicts(List<Shift> shifts, Employer employer) {
        Map<Shift, Employee> assignedShifts = new HashMap<>();
        int maxAttempts = shifts.size();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            boolean progressMade = false;

            for (Shift shift : shifts) {
                if (!assignedShifts.containsKey(shift) && findMatching(shift, assignedShifts, new HashSet<>())) {
                    progressMade = true;
                }
            }

            if (!progressMade) {
                break;
            }
        }

        if (!conflictGraph.getAllConflicts().isEmpty()) {
            System.out.println("Unresolved conflicts remain: " + conflictGraph.getAllConflicts());
        }
    }

    private boolean findMatching(Shift shift, Map<Shift, Employee> assignedShifts, Set<Employee> visited) {
        for (Employee employee : shift.getAssignedEmployees()) {
            if (!visited.contains(employee)) {
                visited.add(employee);

                if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().contains(employee.getRole())) {
                    continue;
                }

                for (Shift conflictingShift : conflictGraph.getConflicts(shift)) {
                    if (!assignedShifts.containsKey(conflictingShift) || 
                        findMatching(conflictingShift, assignedShifts, visited)) {
                        assignedShifts.put(shift, employee);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAvailable(Employee employee, Shift shift) {
        if (employee.getAvailability() == null || !employee.getAvailability().contains(shift.getDate())) {
            return false;
        }

        if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().contains(employee.getRole())) {
            return false;
        }

        for (Shift assignedShift : employee.getAssignedShifts()) {
            boolean overlaps = shift.getStartTime().compareTo(assignedShift.getEndTime()) < 0 &&
                               shift.getEndTime().compareTo(assignedShift.getStartTime()) > 0;
            if (overlaps && !employee.getEmployer().isFlexibleWorkHours()) {
                return false;
            }
        }
        return true;
    }

    private boolean canWork(Employee employee, Employer employer, Shift shift) {
        if (shift.isNightShift() && !employer.isAllowNightShifts()) {
            return false;
        }

        if (employer.isFlexibleWorkHours()) {
            List<Shift> allShifts = new ArrayList<>(employee.getAssignedShifts());
            allShifts.add(shift);

            int totalDistinctHours = calculateDistinctHours(allShifts);
            return totalDistinctHours <= employer.getMaxShiftsPerWeek();
        }

        return (employee.getCurrentWorkHours() + shift.getDuration() <= employer.getMaxWorkHoursPerDay()) &&
               (employee.getTotalWorkHoursThisWeek() + shift.getDuration() <= employer.getMaxShiftsPerWeek());
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

        List<int[]> intervals = new ArrayList<>();
        for (Shift shift : shifts) {
            intervals.add(new int[] {
                parseTime(shift.getStartTime()),
                parseTime(shift.getEndTime())
            });
        }

        intervals.sort(Comparator.comparingInt(a -> a[0]));
        int totalMinutes = 0;
        int[] currentInterval = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            int[] nextInterval = intervals.get(i);
            if (currentInterval[1] >= nextInterval[0]) {
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else {
                totalMinutes += (currentInterval[1] - currentInterval[0]);
                currentInterval = nextInterval;
            }
        }
        totalMinutes += (currentInterval[1] - currentInterval[0]);
        return totalMinutes / 60;
    }

    private int parseTime(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}


