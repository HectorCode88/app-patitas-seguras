package pe.edu.upc.patitas.login.payloads.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(max = 20)
  private String name;

  @NotBlank
  @Size(max = 20)
  private String lastname;

  @NotBlank
  @Size(max = 20)
  private String documentType;

  @NotBlank
  @Size(max = 20)
  private String documentNumbers;

  @NotBlank
  @Size(max = 20)
  private String phoneNumber;

  @NotBlank
  @Size(max = 20)
  private String district;

  @NotBlank
  @Size(max = 20)
  private String address;

  @NotBlank
  @Size(max = 20)
  private String houseType;

}
