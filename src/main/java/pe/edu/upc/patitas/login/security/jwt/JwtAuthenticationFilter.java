package pe.edu.upc.patitas.login.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import pe.edu.upc.patitas.login.security.service.UserDetailsImpl;
import pe.edu.upc.patitas.login.security.service.UserDetailsServiceImpl;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String username = jwtUtils.getSubjectFromJwtToken(jwtUtils.getJwtFromHeader(request));

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,
        userDetails.getAuthorities());

    //usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    var userDetails = (UserDetailsImpl) authResult.getPrincipal();
    String jwt = jwtUtils.generateTokenFromUsernameAndExtras(userDetails.getUsername(), Map.of("email", userDetails.getEmail()));

    response.addHeader("Authorization", "Bearer " + jwt);
    response.getWriter().flush();

    super.successfulAuthentication(request, response, chain, authResult);
  }

  @Override
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }
}
