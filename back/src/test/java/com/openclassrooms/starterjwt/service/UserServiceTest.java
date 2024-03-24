package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testDeleteUser() {

        // Given
        Long userId = 1L;

        // When
        userService.delete(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testFindUserByIdWhenExists() {

        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertThat(userId).isEqualTo(foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testFindUserByIdWhenNotExists() {

        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertThat(foundUser).isNull();
        verify(userRepository, times(1)).findById(userId);
    }
}
