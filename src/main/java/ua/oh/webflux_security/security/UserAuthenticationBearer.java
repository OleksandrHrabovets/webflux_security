package ua.oh.webflux_security.security;

import io.jsonwebtoken.Claims;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.security.JwtHandler.VerificationResult;

public class UserAuthenticationBearer {

  public static Mono<Authentication> create(VerificationResult verificationResult) {

    Claims claims = verificationResult.claims;
    String subject = claims.getSubject();
    String username = claims.get("username", String.class);
    String role = claims.get("role", String.class);

    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

    Long principalId = Long.parseLong(subject);
    CustomPrincipal principal = new CustomPrincipal(principalId, username);

    return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));

  }

}
