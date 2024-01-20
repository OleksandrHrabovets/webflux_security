package ua.oh.webflux_security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.security.AuthenticationManager;
import ua.oh.webflux_security.security.BearerTokenServerAuthenticationConverter;
import ua.oh.webflux_security.security.JwtHandler;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Value("${jwt.secret}")
  private String secret;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
      AuthenticationManager authenticationManager) {
    String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

    return http
        .csrf(CsrfSpec::disable)
        .authorizeExchange(authExchangeSpec -> {
          authExchangeSpec.pathMatchers(publicRoutes).permitAll();
          authExchangeSpec.anyExchange().authenticated();
        })
        .exceptionHandling(exceptionHandlingSpec -> {
              exceptionHandlingSpec.authenticationEntryPoint((exchange, ex) ->
                  Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(
                      HttpStatus.UNAUTHORIZED))
              );
              exceptionHandlingSpec.accessDeniedHandler((exchange, denied) ->
                  Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(
                      HttpStatus.FORBIDDEN))
              );
            }
        )
        .addFilterAt(bearerAuthenticationWebFilter(authenticationManager),
            SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }

  @Bean
  public AuthenticationWebFilter bearerAuthenticationWebFilter(
      AuthenticationManager authenticationManager) {
    AuthenticationWebFilter bearerAuthenticationWebFilter = new AuthenticationWebFilter(
        authenticationManager);
    bearerAuthenticationWebFilter.setServerAuthenticationConverter(
        new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
    bearerAuthenticationWebFilter.setRequiresAuthenticationMatcher(
        ServerWebExchangeMatchers.pathMatchers("/**"));

    return bearerAuthenticationWebFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
