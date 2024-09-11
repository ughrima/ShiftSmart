package backend.example.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import backend.example.backend.Entity.Employer;

@Repository
public interface EmployerRepository extends MongoRepository<Employer, String> {
    Employer findByEmail(String email); 
    //find employer by email
}