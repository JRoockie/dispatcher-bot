package org.voetsky.dispatcherBot.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.voetsky.dispatcherBot.config.UserAuthenticationProvider;
import org.voetsky.dispatcherBot.dtos.CredentialsDto;
import org.voetsky.dispatcherBot.dtos.SignUpDto;
import org.voetsky.dispatcherBot.dtos.UserDto;
import org.voetsky.dispatcherBot.services.UserService.UserOperationsService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/bot")
public class AuthController {

    private final UserOperationsService userOperationsService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userOperationsService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userOperationsService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

}
