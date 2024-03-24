package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testLoadUserByUsernameWhenUserExists() {

        // Given
        String email = "test@test.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("Alex");
        user.setLastName("Test");
        user.setPassword("1234");

        // When
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        // Then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getFirstName()).isEqualTo("Alex");
        assertThat(userDetails.getLastName()).isEqualTo("Test");
        assertThat(userDetails.getPassword()).isEqualTo("1234");
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsernameWhenUserNotExists() {

        // Given
        String email = "test@test.com";

        // When
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            userDetailsService.loadUserByUsername(email);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: %s", email);
        verify(userRepository, times(1)).findByEmail(email);
    }
}
