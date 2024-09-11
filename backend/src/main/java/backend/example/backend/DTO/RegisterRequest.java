package backend.example.backend.DTO;

import org.springframework.web.multipart.MultipartFile;

import backend.example.backend.Entity.Company;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String companyName;
    private Company companyPolicy;  
    private MultipartFile employeeExcel; 

}
