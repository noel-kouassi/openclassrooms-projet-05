package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void testFindById() {

        // Given
        long id = 1L;
        User user = new User();
        user.setId(id);
        when(userService.findById(id)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    public void testFindById_NotFound() {

        // Given
        long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = userController.findById(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, times(1)).findById(id);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testFindById_InvalidId() {

        // Given
        String id = "badId";

        // When
        ResponseEntity<?> responseEntity = userController.findById(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).findById(anyLong());
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    public void testDelete_NotFound() {

        // Given
        long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = userController.save(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, never()).delete(id);
    }

    @Test
    public void testDelete_InvalidId() {

        // Given
        String id = "badId";

        // When
        ResponseEntity<?> responseEntity = userController.save(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).delete(anyLong());
    }
}
