package ua.oh.webflux_security.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.model.User;
import ua.oh.webflux_security.service.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiration}")
  private Integer expirationInSeconds;
  @Value("${jwt.issuer}")
  private String issuer;

  public Mono<TokenDetails> authenticate(String username, String password) {

    return userService.findByUsername(username)
        .flatMap(user -> {
          if (!user.isEnabled()) {
            return Mono.error(() -> new RuntimeException("User is not enabled"));
          }
          if (!passwordEncoder.matches(password, user.getPassword())) {
            return Mono.error(() -> new RuntimeException("Wrong password"));
          }
          return Mono.just(generateToken(user).toBuilder()
              .userId(user.getId())
              .build());
        })
        .switchIfEmpty(Mono.error(() -> new RuntimeException("User not found")));
  }

  private TokenDetails generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("role", user.getRole());
    return generateToken(claims, user.getId().toString());
  }

  private TokenDetails generateToken(Map<String, Object> claims, String subject) {
    long expirationInMillis = expirationInSeconds * 1000L;
    Date expirationDate = new Date(new Date().getTime() + expirationInMillis);

    return generateToken(expirationDate, claims, subject);
  }

  private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims,
      String subject) {
    Date created = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuer(issuer)
        .setIssuedAt(created)
        .setId(UUID.randomUUID().toString())
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
        .compact();
    return TokenDetails.builder()
        .token(token)
        .issuedAt(created)
        .expiresAt(expirationDate)
        .build();
  }

}
