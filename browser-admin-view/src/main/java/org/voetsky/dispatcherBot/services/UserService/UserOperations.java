package org.voetsky.dispatcherBot.services.UserService;

import org.voetsky.dispatcherBot.dtos.CredentialsDto;
import org.voetsky.dispatcherBot.dtos.SignUpDto;
import org.voetsky.dispatcherBot.dtos.UserDto;

public interface UserOperations {

    UserDto login(CredentialsDto credentialsDto);

    void registerAdmin();

    UserDto register(SignUpDto userDto);

    UserDto findByLogin(String login);
}



