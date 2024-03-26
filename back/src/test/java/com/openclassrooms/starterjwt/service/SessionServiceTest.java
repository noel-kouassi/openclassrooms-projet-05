package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void testCreateSession() {

        // Given
        Session session = new Session();

        // When
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        // Then
        assertThat(createdSession).isNotNull();
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDeleteSession() {

        // Given
        Long sessionId = 1L;

        // When
        sessionService.delete(sessionId);

        // Then
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    public void testFindAllSessions() {

        // Given
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());

        // When
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> foundSessions = sessionService.findAll();

        // Then
        assertThat(foundSessions).hasSize(2);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetSessionByIdWhenExists() {

        // Given
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        // When
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(sessionId);

        // Then
        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getId()).isEqualTo(sessionId);
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testGetSessionByIdWhenNotExists() {

        // Given
        Long sessionId = 1L;

        // When
        when(sessionRepository.findById(sessionId)).thenReturn(empty());

        Session foundSession = sessionService.getById(sessionId);

        // Then
        assertThat(foundSession).isNull();
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testUpdateSession() {

        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Session");

        // When
        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(1L, session);

        // Then
        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getId()).isEqualTo(1L);
        assertThat(updatedSession.getName()).isEqualTo("Session");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipateSession() {

        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(2L);

        List<User> users = new ArrayList<>();
        users.add(user);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(users);

        // When
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        // Then
        assertThat(session.getUsers()).contains(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipateSessionAlreadyParticipated() {

        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(singletonList(user));

        // When
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Throwable thrown = catchThrowable(() -> {
            sessionService.participate(sessionId, userId);
        });

        // Then
        assertThat(thrown).isInstanceOf(BadRequestException.class);
        verify(sessionRepository, never()).save(session);
    }

    @Test
    public void testNoLongerParticipateSession() {

        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(singletonList(user));

        // When
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        // Then
        assertThat(session.getUsers()).doesNotContain(user);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipateSessionNotParticipated() {

        // Given
        Long sessionId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(2L);
        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // When
        Throwable thrown = catchThrowable(() -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });

        // Then
        assertThat(thrown).isInstanceOf(BadRequestException.class);
        verify(sessionRepository, never()).save(session);
    }
}
