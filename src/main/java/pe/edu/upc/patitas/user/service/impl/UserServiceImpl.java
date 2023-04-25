package pe.edu.upc.patitas.user.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upc.patitas.login.payloads.response.UserInfoResponse;
import pe.edu.upc.patitas.login.repository.UserRepository;
import pe.edu.upc.patitas.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public List<UserInfoResponse> getAllUsers() {
    return userRepository.findAll()
      .stream()
      .map(user -> UserInfoResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .roles(user.getRoles()
          .stream()
          .map(role -> role.getName().name())
          .collect(Collectors.toList()))
        .build())
      .collect(Collectors.toList());
  }
}
