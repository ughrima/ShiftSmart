package backend.example.backend.DTO;

public class JWTAuthResponse {
    private String accessToken;
    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
}