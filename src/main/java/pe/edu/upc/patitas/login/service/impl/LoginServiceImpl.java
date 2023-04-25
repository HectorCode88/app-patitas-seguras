package pe.edu.upc.patitas.login.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

@Service
public class LoginServiceImpl implements LoginService {

 /*@Autowired
  AuthenticationManager authenticationManager;*/

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  JwtUtils jwtUtils;

  @Override
  public UserInfoResponse authenticateUser(LoginRequest loginRequest) {

    System.out.println("encodePassword ->> " + loginRequest.getPassword());

    return userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword())
      .map(user -> new UserInfoResponse(user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList())))
      .orElseThrow();
  }

  @Override
  public User registerUser(SignupRequest signUpRequest) {
/*    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }*/

    // Create new user's account
    User user = User.builder()
      .username(signUpRequest.getUsername())
      .password(signUpRequest.getPassword())
      .email(signUpRequest.getEmail())
      .name(signUpRequest.getName())
      .lastname(signUpRequest.getLastname())
      .documentType(signUpRequest.getDocumentType())
      .documentNumbers(signUpRequest.getDocumentNumbers())
      .phoneNumber(signUpRequest.getPhoneNumber())
      .district(signUpRequest.getDistrict())
      .address(signUpRequest.getAddress())
      .houseType(signUpRequest.getHouseType())
      .build();
    //encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return user;
  }

  @Override
  public ResponseEntity<MessageResponse> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(new MessageResponse("You've been signed out!"));
  }

}
