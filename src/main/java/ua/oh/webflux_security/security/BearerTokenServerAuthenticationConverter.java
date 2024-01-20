package ua.oh.webflux_security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

  private final JwtHandler jwtHandler;
  public static final String BEARER_PREFIX = "Bearer ";

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    return extractHeader(exchange)
        .flatMap(s -> Mono.justOrEmpty(s.substring(BEARER_PREFIX.length())))
        .flatMap(jwtHandler::check)
        .flatMap(UserAuthenticationBearer::create);
  }

  private Mono<String> extractHeader(ServerWebExchange exchange) {
    return Mono.justOrEmpty(exchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION));
  }

}
