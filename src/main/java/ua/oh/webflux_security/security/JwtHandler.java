package ua.oh.webflux_security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Base64;
import java.util.Date;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.exception.UnauthorizedException;

public class JwtHandler {

  private final String secret;

  public JwtHandler(String secret) {
    this.secret = secret;
  }

  public Mono<VerificationResult> check(String accessToken) {

    return Mono.just(verify(accessToken))
        .onErrorResume(e -> Mono.error(new UnauthorizedException("Unauthorized")));

  }

  private VerificationResult verify(String token) {

    Claims claims = getClaimsFromToken(token);
    final Date expirationDate = claims.getExpiration();

    if (expirationDate.before(new Date())) {
      throw new RuntimeException("Token expired");
    }

    return new VerificationResult(claims, token);

  }

  private Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
        .parseClaimsJws(token)
        .getBody();
  }

  public static class VerificationResult {

    public Claims claims;
    public String token;

    public VerificationResult(Claims claims, String token) {
      this.claims = claims;
      this.token = token;
    }
  }
}
