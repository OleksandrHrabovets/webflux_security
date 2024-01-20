package ua.oh.webflux_security.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ua.oh.webflux_security.dto.UserDto;
import ua.oh.webflux_security.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto map(User user);

  @InheritInverseConfiguration
  User map(UserDto dto);

}
