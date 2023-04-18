package pe.edu.upc.patitas.login.payloads.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
  @NotBlank
  private String username;

  @NotBlank
  private String password;
}
