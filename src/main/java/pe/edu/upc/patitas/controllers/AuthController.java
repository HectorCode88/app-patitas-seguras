package pe.edu.upc.patitas.controllers;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  /*@Autowired
  AuthenticationManager authenticationManager;*/

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    /*Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginRequest
        .getUsername(),
        loginRequest
          .getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
      .map(item -> item.getAuthority())
      .collect(Collectors.toList());*/

    //String encodePassword = encoder.encode(loginRequest.getPassword());

    System.out.println("encodePassword ->> " + loginRequest.getPassword());

    return userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword())
      .map(user -> new UserInfoResponse(user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList())))
      .map(userInfoResponse -> ResponseEntity.ok().body(userInfoResponse)).get();

    /*return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
      .body(new UserInfoResponse(userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles));*/
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

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

    //return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    return ResponseEntity.ok(user);
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
      .body(new MessageResponse("You've been signed out!"));
  }
}
