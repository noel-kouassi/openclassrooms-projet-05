package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testAuthenticateUser() {

        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "Alex", "Test", false, "1234");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        User user = new User();
        user.setAdmin(false);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        String jwtToken = "jwtToken";
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwtToken);

        // When
        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        // Then
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.getToken()).isEqualTo(jwtToken);
        assertThat(jwtResponse.getId()).isEqualTo(userDetails.getId());
        assertThat(jwtResponse.getUsername()).isEqualTo(userDetails.getUsername());
        assertThat(jwtResponse.getFirstName()).isEqualTo(userDetails.getFirstName());
        assertThat(jwtResponse.getLastName()).isEqualTo(userDetails.getLastName());
        assertThat(jwtResponse.getAdmin()).isEqualTo(user.isAdmin());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail("test@test.com");
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }

    @Test
    public void testRegisterUser() {

        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Alex");
        signupRequest.setLastName("Test");
        signupRequest.setPassword("password");

        // When
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("11jest598");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Then
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(messageResponse).isNotNull();
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");

        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_WhenEmailAlreadyTaken() {

        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setFirstName("Alex");
        signupRequest.setLastName("Test");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        // When
        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        // Then
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(messageResponse).isNotNull();
        assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");

        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(userRepository, times(0)).save(any(User.class));
    }
}
