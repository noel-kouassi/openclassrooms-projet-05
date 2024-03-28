package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserControllerIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Autowired
    private AuthController authController;

    @Test
    public void testFindById() {

        // Given
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Alex");
        user.setEmail("test@test.com");
        user.setPassword("password1234");
        user.setUpdatedAt(now());
        user.setCreatedAt(now());
        User userSaved = userRepository.save(user);

        // When
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(userSaved.getId()));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(UserDto.class);

        UserDto userDto = (UserDto) responseEntity.getBody();
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(userSaved.getId());
        assertThat(userDto.getFirstName()).isEqualTo("Test");
        assertThat(userDto.getLastName()).isEqualTo("Alex");
        assertThat(userDto.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    public void testFindById_WhenUserNotExists() {

        // Given
        String id = "1";

        // When
        ResponseEntity<?> responseEntity = userController.findById(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testDeleteUser() {

        // Given
        String email = "test@test.com";
        String password = "password1234";

        User user = new User(email, "Alex", "Test", passwordEncoder.encode(password), false);
        User userSaved = userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        authController.authenticateUser(loginRequest);

        // When
        ResponseEntity<?> responseEntity = userController.delete(String.valueOf(userSaved.getId()));
        Optional<User> optionalUser = userRepository.findById(userSaved.getId());

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
        assertThat(optionalUser.isPresent()).isFalse();
    }
}
