package ua.oh.webflux_security.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.model.User;

public interface UserRepository extends R2dbcRepository<User, Long> {

  Mono<User> findByUsername(String username);

}
