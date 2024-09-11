package backend.example.backend.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import backend.example.backend.Entity.Company;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    Company findByEmployerId(String employerId); 
}
