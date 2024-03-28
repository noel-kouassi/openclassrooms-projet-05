package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SessionControllerIntegrationTest {

    private final List<User> users = new ArrayList<>();
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionController sessionController;
    @Autowired
    private SessionMapper sessionMapper;
    private Teacher teacherSaved;

    @BeforeEach
    public void setUp() {

        Teacher teacher = new Teacher()
                .setLastName("Test")
                .setFirstName("Alex")
                .setCreatedAt(now())
                .setUpdatedAt(now());

        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());

        teacherSaved = teacherRepository.save(teacher);
        User userSaved = userRepository.save(user);
        users.add(userSaved);
    }

    @Test
    public void testFindById() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session savedSession = sessionRepository.save(session);

        // When
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(savedSession.getId()));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(SessionDto.class);

        SessionDto sessionDto = (SessionDto) responseEntity.getBody();
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getId()).isNotNull();
        assertThat(sessionDto.getName()).isEqualTo("session test");
        assertThat(sessionDto.getDescription()).isEqualTo("session test");
    }

    @Test
    public void testFindById_WhenSessionNotExists() {

        // Given
        String sessionId = "1";

        // When
        ResponseEntity<?> responseEntity = sessionController.findById(sessionId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testFindAll() {

        // Given
        Session session1 = new Session()
                .setName("session 1")
                .setDescription("session 1")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session firstSession = sessionRepository.save(session1);

        Session session2 = new Session()
                .setName("session 2")
                .setDescription("session 2")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session secondSession = sessionRepository.save(session2);

        // When
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(ArrayList.class);

        List<SessionDto> sessionDtos = (List<SessionDto>) responseEntity.getBody();
        assertThat(sessionDtos).isNotEmpty();
        assertThat(sessionDtos.size()).isEqualTo(2);
        assertThat(sessionDtos.stream().map(SessionDto::getId)).containsOnly(firstSession.getId(), secondSession.getId());
    }

    @Test
    public void testUpdate() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session savedSession = sessionRepository.save(session);

        SessionDto sessionDtoUpdated = sessionMapper.toDto(savedSession);
        sessionDtoUpdated.setDescription("description updated");

        // When
        ResponseEntity<?> responseEntity = sessionController.update(String.valueOf(savedSession.getId()), sessionDtoUpdated);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(SessionDto.class);

        SessionDto sessionDto = (SessionDto) responseEntity.getBody();
        assertThat(sessionDto).isNotNull();
        assertThat(sessionDto.getId()).isEqualTo(savedSession.getId());
        assertThat(sessionDto.getDescription()).isEqualTo("description updated");
    }

    @Test
    public void testDelete() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session savedSession = sessionRepository.save(session);

        // When
        ResponseEntity<?> responseEntity = sessionController.delete(String.valueOf(savedSession.getId()));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testParticipate() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session savedSession = sessionRepository.save(session);

        User secondUser = new User()
                .setFirstName("Jean")
                .setLastName("Smart")
                .setAdmin(false)
                .setEmail("smart@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());

        User savedSecondUser = userRepository.save(secondUser);

        // When
        ResponseEntity<?> responseEntity = sessionController.participate(String.valueOf(savedSession.getId()), String.valueOf(savedSecondUser.getId()));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testNoLongerParticipate() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);
        Session savedSession = sessionRepository.save(session);
        User removedUser = users.get(0);

        // When
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(String.valueOf(savedSession.getId()), String.valueOf(removedUser.getId()));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }
}
