package backend.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.example.backend.Entity.Employee;
import backend.example.backend.Repository.EmployeeRepository;

import java.util.List;

//fetches, adds and updates employees
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //get all employees of an employer
    public List<Employee> getEmployeesByEmployer(String employerId) {
        return employeeRepository.findByEmployerId(employerId);
    }

    // add employees
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    //update employee details
    public Employee updateEmployee(String id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setAvailability(updatedEmployee.getAvailability());
        employee.setPreferences(updatedEmployee.getPreferences());
        employee.setSkills(updatedEmployee.getSkills());
        return employeeRepository.save(employee);
    }
}
