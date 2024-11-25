package backend.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Shift> getAllShiftsWithEmployees() {
        return mongoTemplate.findAll(Shift.class);
    }


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



    private void validateShift(Shift shift) {
        if (shift == null || shift.getId() == null || shift.getDate() == null || 
            shift.getStartTime() == null || shift.getEndTime() == null) {
            throw new IllegalArgumentException("Each shift must have a valid ID, date, start time, and end time.");
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

    private void resolveConflicts(List<Shift> shifts, Employer employer) {
        Map<Shift, Employee> assignedShifts = new HashMap<>();
    
        for (Shift shift : shifts) {
            // Check if the shift conflicts with others
            for (Shift conflictShift : conflictGraph.getConflicts(shift)) {
                // Resolve conflicts
                if (!assignedShifts.containsKey(conflictShift)) {
                    // Try assigning employees to the conflicting shift
                    findMatching(shift, assignedShifts, new HashSet<>());
                }
            }
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


    private boolean hasConflict(Shift shift1, Shift shift2) {
        for (Employee employee : shift1.getAssignedEmployees()) {
            if (shift2.getAssignedEmployees().contains(employee)) {
                return true;
            }
        }
        return false;
    }

@Transactional
public void assignShifts(List<Shift> shifts, Employer employer) {
    System.out.println("Inside assignShifts method...");

    if (shifts == null || shifts.isEmpty()) {
        throw new IllegalArgumentException("Shifts list cannot be null or empty.");
    }
    if (employer == null) {
        throw new IllegalArgumentException("Employer cannot be null.");
    }

    List<Employee> employees = employeeRepository.findByEmployer_Id(employer.getId());
    if (employees == null || employees.isEmpty()) {
        throw new IllegalArgumentException("No employees available for scheduling.");
    }

    PriorityQueue<Employee> availableEmployees = initializePriorityQueue(employees);

    for (Shift shift : shifts) {
        validateShift(shift);
        shift.setEmployer(employer);  // Ensure employer is set

        // Re-fetch the shift from the database to ensure we have the latest version
        Shift shiftFromDB = shiftRepository.findById(shift.getId()).orElseThrow(() -> new IllegalArgumentException("Shift not found"));

        
        // Debugging log for checking assigned employees size before assignment
        System.out.println("Before assigning employees, shift ID " + shiftFromDB.getId() + " has " + shiftFromDB.getAssignedEmployees().size() + " assigned employees.");
        System.out.println("Max Employees: " + shiftFromDB.getMaxEmployees());

        // Check if shift is already full before attempting assignment
        if (shiftFromDB.getAssignedEmployees().size() >= shiftFromDB.getMaxEmployees()) {
            System.out.println("Shift " + shiftFromDB.getId() + " is already full.");
            continue; // Skip if shift is full
        }

        boolean assigned = false;

        // Attempt to assign employees
        for (Employee employee : availableEmployees) {
            System.out.println("Attempting to assign employee: " + employee.getName() + " to shift: " + shiftFromDB.getId());

            // Check if the employee is available and can work the shift
            if (isAvailable(employee, shiftFromDB) && canWork(employee, employer, shiftFromDB)) {
                System.out.println("Assigning employee " + employee.getName() + " to shift " + shiftFromDB.getId());
                assignEmployeeToShift(employee, shiftFromDB);
                availableEmployees.remove(employee); // Re-add employee to queue after updating their hours
                availableEmployees.add(employee);
                assigned = true;
                break; // Exit after assigning one employee
            } else {
                logEmployeeSkipReason(employee, shiftFromDB);
            }
        }

        // If no employee could be assigned, add conflict
        if (!assigned) {
            addShiftConflicts(shiftFromDB, shifts);
        }

        // After assigning employees, check again if the shift is full and save the shift
        System.out.println("After assigning employees, shift ID " + shiftFromDB.getId() + " has " + shiftFromDB.getAssignedEmployees().size() + " assigned employees.");
        if (shiftFromDB.getAssignedEmployees().size() < shiftFromDB.getMaxEmployees()) {
            shiftRepository.save(shiftFromDB);  // Save shift only if it's not full
            System.out.println("Shift saved successfully with assigned employees for shift ID: " + shiftFromDB.getId());
        } else {
            System.out.println("Skipping save, shift " + shiftFromDB.getId() + " is full.");
        }
    }

    // Resolve conflicts after assigning all shifts
    resolveConflicts(shifts, employer);
}

private void assignEmployeeToShift(Employee employee, Shift shift) {
    // Log before adding employee to assignedEmployees
    System.out.println("Assigned Employees before adding: " + shift.getAssignedEmployees().size());
    
    // Check if the employee is already assigned to the shift
    if (!shift.getAssignedEmployees().contains(employee)) {
        shift.getAssignedEmployees().add(employee);  // Adding employee to the shift
        System.out.println("Added employee " + employee.getName() + " to shift " + shift.getId());
    }
    
    // Log after adding employee
    System.out.println("Assigned Employees after adding: " + shift.getAssignedEmployees().size());
    
    // Update the employee's assigned shifts list
    if (!employee.getAssignedShifts().contains(shift)) {
        employee.getAssignedShifts().add(shift);  // Adding shift to the employee
    }
    
    // Save both the shift and the employee (ensure both are saved after changes)
    shiftRepository.save(shift);
    employeeRepository.save(employee);
    
    // Log for debugging after saving
    System.out.println("Assigned employee " + employee.getName() + " to shift " + shift.getId());
    System.out.println("Employee's assigned shifts: " + employee.getAssignedShifts().size());
    System.out.println("Shift's assigned employees: " + shift.getAssignedEmployees().size());
}



private boolean isAvailable(Employee employee, Shift shift) {
    // No need to check the availability against the shift's date now
    System.out.println("Employee " + employee.getName() + " is available for shift: " + shift.getId());
    
    // Check if the shift has any required roles. If it doesn't, skip the role check.
    if (shift.getRequiredRoles() != null && !shift.getRequiredRoles().isEmpty()) {
        // If roles are defined, check if the employee's role matches the shift's required role
        if (!shift.getRequiredRoles().contains(employee.getRole())) {
            System.out.println("Employee " + employee.getName() + " does not have the required role for shift: " + shift.getId());
            return false; // Employee does not have the required role
        }
    }

    // If no roles are required or the employee matches the required role, proceed
    return true; // Employee is available for the shift
}




    private boolean canWork(Employee employee, Employer employer, Shift shift) {
        System.out.println("Checking work hours for " + employee.getName());
        
        if (shift.isNightShift() && !employer.isAllowNightShifts()) {
            System.out.println("Employee " + employee.getName() + " cannot work night shift.");
            return false;
        }
        
        // Check if work hours for the day exceed max limit
        if (employee.getCurrentWorkHours() + shift.getDuration() > employer.getMaxWorkHoursPerDay()) {
            System.out.println("Employee " + employee.getName() + " exceeds max work hours for the day.");
            return false;
        }
    
        // Check if work hours for the week exceed max limit
        if (employee.getTotalWorkHoursThisWeek() + shift.getDuration() > employer.getMaxShiftsPerWeek()) {
            System.out.println("Employee " + employee.getName() + " exceeds max shifts for the week.");
            return false;
        }
    
        return true;
    }
    
    private void logEmployeeSkipReason(Employee employee, Shift shift) {
        if (!isAvailable(employee, shift)) {
            System.out.println("Employee " + employee.getName() + " is not available for shift: " + shift.getId());
        }
        if (!canWork(employee, shift.getEmployer(), shift)) {
            System.out.println("Employee " + employee.getName() + " cannot work shift " + shift.getId() + " due to work hour or role constraints.");
        }
    }
    

}


