package ua.oh.webflux_security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.exception.UnauthorizedException;
import ua.oh.webflux_security.service.UserService;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private final UserService userService;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
    return userService.findById(principal.getId())
        .switchIfEmpty(Mono.error(new UnauthorizedException("Unauthorized")))
        .map(user -> authentication);
  }

}
