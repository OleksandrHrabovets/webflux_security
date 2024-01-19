package ua.oh.webflux_security.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

  private Long id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private Role role;
  private boolean enabled;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @ToString.Include(name = "password")
  private String maskPassword() {
    return "********";
  }

}
