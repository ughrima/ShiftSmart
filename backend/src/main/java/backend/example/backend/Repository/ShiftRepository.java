package backend.example.backend.Repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import backend.example.backend.Entity.Shift;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, String> {
    List<Shift> findByEmployerId(String employerId);  
    // Retrieves shifts assigned to a specific employer
}
