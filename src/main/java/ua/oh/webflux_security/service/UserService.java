package ua.oh.webflux_security.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.model.Role;
import ua.oh.webflux_security.model.User;
import ua.oh.webflux_security.repo.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Mono<User> findById(Long id) {

    return userRepository.findById(id);

  }

  public Mono<User> findByUsername(String username) {

    return userRepository.findByUsername(username);

  }

  public Mono<User> register(User user) {

    return userRepository.save(user.toBuilder()
        .password(passwordEncoder.encode(user.getPassword()))
        .role(Role.USER)
        .enabled(true)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build()
    ).doOnSuccess(u ->
        log.info("User registered successfully: {}", u));
  }

}
