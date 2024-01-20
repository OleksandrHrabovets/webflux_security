package ua.oh.webflux_security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.oh.webflux_security.dto.AuthRequestDto;
import ua.oh.webflux_security.dto.AuthResponseDto;
import ua.oh.webflux_security.dto.UserDto;
import ua.oh.webflux_security.mapper.UserMapper;
import ua.oh.webflux_security.model.User;
import ua.oh.webflux_security.repo.UserRepository;
import ua.oh.webflux_security.security.CustomPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @PostMapping("/register")
  public Mono<UserDto> register(@RequestBody UserDto dto) {
    User user = userMapper.map(dto);
    return userRepository.save(user).map(userMapper::map);
  }

  @PostMapping("/login")
  public Mono<AuthResponseDto> register(@RequestBody AuthRequestDto dto) {
    return null;
  }

  @GetMapping("/info")
  public Mono<UserDto> info(Authentication authentication) {
    CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
    return userRepository.findById(principal.getId()).map(userMapper::map);
  }

}