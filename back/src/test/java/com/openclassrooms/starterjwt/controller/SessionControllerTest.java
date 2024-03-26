package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @Test
    public void testFindById_ExistingSession() {

        // Given
        long id = 1L;
        Session session = new Session();
        session.setId(id);
        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).getById(id);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testFindById_SessionNotFound() {

        // Given
        long id = 1L;
        when(sessionService.getById(id)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(sessionService, times(1)).getById(id);
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testFindById_InvalidId() {

        // Given
        String id = "badId";

        // When
        ResponseEntity<?> responseEntity = sessionController.findById(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).getById(anyLong());
        verify(sessionMapper, never()).toDto(any(Session.class));
    }

    @Test
    public void testFindAll_SessionsFound() {

        // Given
        List<Session> sessions = Collections.singletonList(new Session());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test
    public void testFindAll_NoSessionsFound() {

        // Given
        when(sessionService.findAll()).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(Collections.emptyList());
    }

    @Test
    public void testCreate_ValidSessionDto() {

        // Given
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // When
        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).create(session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testUpdate_ValidSessionDto() {

        // Given
        long id = 1L;
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // When
        ResponseEntity<?> responseEntity = sessionController.update(String.valueOf(id), sessionDto);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).update(id, session);
        verify(sessionMapper, times(1)).toDto(session);
    }

    @Test
    public void testDelete_ValidId() {

        // Given
        long id = 1L;
        Session session = new Session();
        when(sessionService.getById(id)).thenReturn(session);

        // When
        ResponseEntity<?> responseEntity = sessionController.save(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).getById(id);
        verify(sessionService, times(1)).delete(id);
    }

    @Test
    public void testDelete_InvalidId() {

        // Given
        long id = 1L;
        when(sessionService.getById(id)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = sessionController.save(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(sessionService, times(1)).getById(id);
        verify(sessionService, never()).delete(any(Long.class));
    }

    @Test
    public void testDelete_InvalidIdFormat() {

        // Given
        String id = "badId";

        // When
        ResponseEntity<?> responseEntity = sessionController.save(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).getById(anyLong());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    public void testParticipate_ValidIds() {

        // Given
        long sessionId = 1L;
        long userId = 2L;

        // When
        ResponseEntity<?> responseEntity = sessionController.participate(String.valueOf(sessionId), String.valueOf(userId));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).participate(sessionId, userId);
    }

    @Test
    public void testParticipate_InvalidIds() {

        // Given
        String sessionId = "badSessionId";
        String userId = "badUserId";

        // When
        ResponseEntity<?> responseEntity = sessionController.participate(sessionId, userId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    public void testNoLongerParticipate_ValidIds() {

        // Given
        long sessionId = 1L;
        long userId = 2L;

        // When
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
    }

    @Test
    public void testNoLongerParticipate_InvalidIds() {

        // Given
        String sessionId = "badSessionId";
        String userId = "badUserId";

        // When
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(sessionId, userId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
}
