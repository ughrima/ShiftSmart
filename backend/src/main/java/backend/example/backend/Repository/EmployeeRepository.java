package backend.example.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import backend.example.backend.Entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    List<Employee> findByEmployer_Id(String employerId);
}
