package backend.example.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import backend.example.backend.Entity.Shift;
@Repository
public interface ShiftRepository extends MongoRepository<Shift, String> {
    List<Shift> findByEmployerId(String employerId);
}
