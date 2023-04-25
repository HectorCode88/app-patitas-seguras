package pe.edu.upc.patitas.user.service;

import java.util.List;
import pe.edu.upc.patitas.login.payloads.response.UserInfoResponse;

public interface UserService {

  List<UserInfoResponse> getAllUsers();
}
