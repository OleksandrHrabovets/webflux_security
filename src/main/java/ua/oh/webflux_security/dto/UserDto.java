package ua.oh.webflux_security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Data;
import ua.oh.webflux_security.model.Role;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

  private Long id;
  private String username;
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;
  private String firstName;
  private String lastName;
  private Role role;
  private boolean enabled;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
