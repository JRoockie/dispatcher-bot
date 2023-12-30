package org.voetsky.dispatcherBot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.voetsky.dispatcherBot.dtos.UserDto;
import org.voetsky.dispatcherBot.dtos.SignUpDto;
import org.voetsky.dispatcherBot.entites.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
