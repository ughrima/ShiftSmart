package backend.example.backend.DTO;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class RegisterRequest {
    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String companyName;

    private MultipartFile employeeExcel; // File upload
}
