package org.voetsky.dispatcherBot.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.dtos.CredentialsDto;
import org.voetsky.dispatcherBot.dtos.SignUpDto;
import org.voetsky.dispatcherBot.dtos.UserDto;
import org.voetsky.dispatcherBot.entites.User;
import org.voetsky.dispatcherBot.exceptions.AdminViewException;
import org.voetsky.dispatcherBot.mappers.UserMapper;
import org.voetsky.dispatcherBot.repositories.UserRepository;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AdminViewException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AdminViewException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    @PostConstruct
    public UserDto registerAdmin() {
        User user = new User();
        user.setPassword("u");
        user.setLogin("u");

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(user.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

        if (optionalUser.isPresent()) {
            throw new AdminViewException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AdminViewException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

}
