package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthController authController;

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testAuthenticateUser() {

        // Given
        String email = "test@test.com";
        String password = "password1234";

        User user = new User(email, "Alex", "Test", passwordEncoder.encode(password), false);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // When
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Then
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isInstanceOf(JwtResponse.class);

        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.getUsername()).isEqualTo(email);
        assertThat(jwtResponse.getFirstName()).isEqualTo("Test");
        assertThat(jwtResponse.getLastName()).isEqualTo("Alex");
        assertThat(jwtResponse.getAdmin()).isFalse();
        assertThat(jwtResponse.getToken()).isNotNull();
    }

    @Test
    public void testRegisterUser() {

        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("password1234");
        signupRequest.setLastName("Test");
        signupRequest.setFirstName("Alex");

        // When
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Then
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isInstanceOf(MessageResponse.class);

        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertThat(messageResponse).isNotNull();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
    }

    @Test
    public void testRegisterUser_WhenEmailAlreadyExists() {

        // Given
        String email = "test@test.com";
        String password = "password1234";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        signupRequest.setLastName("Test");
        signupRequest.setFirstName("Alex");

        User user = new User(email, "Test", "Alex", passwordEncoder.encode(password), false);
        userRepository.save(user);

        // When
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Then
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getBody()).isInstanceOf(MessageResponse.class);

        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertThat(messageResponse).isNotNull();
        assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");
    }
}
