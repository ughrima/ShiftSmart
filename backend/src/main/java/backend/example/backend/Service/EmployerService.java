package backend.example.backend.Service;

import backend.example.backend.Entity.Employer;
import backend.example.backend.Repository.EmployerRepository;
import backend.example.backend.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employers
    public List<Employer> getAllEmployers() {
        return employerRepository.findAll();
    }

    // Get employer by ID
    public Optional<Employer> getEmployerById(String id) {
        return employerRepository.findById(id);
    }

    // Add a new employer
    public Employer addEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    // Update an existing employer
    public Employer updateEmployer(String id, Employer updatedEmployer) {
        return employerRepository.findById(id).map(employer -> {
            employer.setEmail(updatedEmployer.getEmail());
            employer.setPassword(updatedEmployer.getPassword());
            employer.setCompanyName(updatedEmployer.getCompanyName());
            return employerRepository.save(employer);
        }).orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    // Delete an employer
    public void deleteEmployer(String id) {
        // Also delete associated employees
        employerRepository.findById(id).ifPresent(employer -> {
            employeeRepository.deleteAll(employer.getEmployees());
            employerRepository.delete(employer);
        });
    }
}
