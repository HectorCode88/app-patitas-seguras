package pe.edu.upc.patitas.login.payloads.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
  private Long id;
  private String username;
  private String email;
  private List<String> roles;
}
