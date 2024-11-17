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

    // Add a new employee to an employer
    public Employee addEmployeeToEmployer(String employerId, Employee employee) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        employee.setEmployer(employer);
        employee = employeeRepository.save(employee);

        employer.getEmployees().add(employee);
        employerRepository.save(employer);

        return employee;
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
}
