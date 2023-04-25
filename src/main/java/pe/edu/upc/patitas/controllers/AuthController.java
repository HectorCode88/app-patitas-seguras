package pe.edu.upc.patitas.controllers;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.patitas.login.models.ERole;
import pe.edu.upc.patitas.login.models.Role;
import pe.edu.upc.patitas.login.models.User;
import pe.edu.upc.patitas.login.payloads.request.LoginRequest;
import pe.edu.upc.patitas.login.payloads.request.SignupRequest;
import pe.edu.upc.patitas.login.payloads.response.MessageResponse;
import pe.edu.upc.patitas.login.payloads.response.UserInfoResponse;
import pe.edu.upc.patitas.login.repository.RoleRepository;
import pe.edu.upc.patitas.login.repository.UserRepository;
import pe.edu.upc.patitas.login.security.jwt.JwtUtils;
import pe.edu.upc.patitas.login.service.LoginService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final LoginService loginService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    return ResponseEntity.ok().body(loginService.authenticateUser(loginRequest));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    return ResponseEntity.ok(loginService.registerUser(signUpRequest));
  }

/*  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(new MessageResponse("You've been signed out!"));
  }*/
}
