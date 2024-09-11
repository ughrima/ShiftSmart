package backend.example.backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.example.backend.Entity.Employee;
import backend.example.backend.Service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //gets a list of employees
    @GetMapping("/{employerId}")
    public ResponseEntity<List<Employee>> getEmployeesByEmployer(@PathVariable String employerId) {
        return ResponseEntity.ok(employeeService.getEmployeesByEmployer(employerId));
    }
    
    //adds an employee
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.addEmployee(employee));
    }

    //update employees
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee updatedEmployee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, updatedEmployee));
    }
}
