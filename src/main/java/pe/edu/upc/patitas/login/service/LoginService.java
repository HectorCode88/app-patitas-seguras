package pe.edu.upc.patitas.login.service;

import org.springframework.http.ResponseEntity;
import pe.edu.upc.patitas.login.models.User;
import pe.edu.upc.patitas.login.payloads.request.LoginRequest;
import pe.edu.upc.patitas.login.payloads.request.SignupRequest;
import pe.edu.upc.patitas.login.payloads.response.MessageResponse;
import pe.edu.upc.patitas.login.payloads.response.UserInfoResponse;

public interface LoginService {

  UserInfoResponse authenticateUser(LoginRequest loginRequest);

  User registerUser(SignupRequest signUpRequest);

  ResponseEntity<MessageResponse> logoutUser();
}
