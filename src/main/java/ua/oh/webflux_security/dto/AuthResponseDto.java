package ua.oh.webflux_security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponseDto {

  private Long userId;
  private String token;
  private Date issuedAt;
  private Date expiresAt;

}
