package backend.example.backend.Service;

import backend.example.backend.Entity.Employee;
import backend.example.backend.Repository.EmployeeRepository;
import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployerRepository employerRepository;

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    // Update an existing employee
    public Employee updateEmployee(String id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setRole(updatedEmployee.getRole());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Delete an employee
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    // public Employee addEmployeeToEmployer(String employerId, Employee employee) {
    //     // Fetch the employer from the database using the provided employerId
    //     Employer employer = employerRepository.findById(employerId)
    //             .orElseThrow(() -> new RuntimeException("Employer not found"));
    
    //     // Set the employer field in the employee object (to ensure proper linkage)
    //     employee.setEmployer(employer);
    
    //     // Save the employee entity first
    //     employee = employeeRepository.save(employee);
    //     System.out.println("Employee saved: " + employee);
    
    //     // Add the newly saved employee to the employer's employees list
    //     employer.getEmployees().add(employee);
    
    //     // Save the employer with the updated employees list
    //     employer = employerRepository.save(employer);
    //     System.out.println("Employer updated with new employee: " + employer);
    
    //     // Fetch the updated employer and verify if the employees list is populated
    //     Employer updatedEmployer = employerRepository.findById(employerId)
    //             .orElseThrow(() -> new RuntimeException("Employer not found"));
    
    //     // Log the updated list of employees to verify it's updated correctly
    //     System.out.println("Updated Employer with employees: " + updatedEmployer.getEmployees());
    
    //     return employee;
    // }
    public Employee addEmployeeToEmployer(String employerId, Employee employee) {
        // Fetch the employer from the database using the provided employerId
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    
        // Set the employer field in the employee object (to ensure proper linkage)
        employee.setEmployer(employer);
    
        // Save the employee entity first
        employee = employeeRepository.save(employee);
    
        // Add the newly saved employee to the employer's employees list
        employer.getEmployees().add(employee);
    
        // Save the employer with the updated employees list
        employerRepository.save(employer);
    
        return employee;
    }
    
    
}

