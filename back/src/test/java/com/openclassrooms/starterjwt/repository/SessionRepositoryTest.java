package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SessionRepositoryTest {

    private final List<User> users = new ArrayList<>();
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
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
    public void testSaveSession() {

        // Given
        Session session = new Session()
                .setName("session test")
                .setDescription("session test")
                .setTeacher(teacherSaved)
                .setCreatedAt(now())
                .setUpdatedAt(now())
                .setDate(new Date())
                .setUsers(users);

        // When
        Session savedSession = sessionRepository.save(session);

        // Then
        assertThat(savedSession.getId()).isNotNull();
    }

    @Test
    public void testUpdateSession() {

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
        savedSession.setName("updated session");
        savedSession.setDescription("updated description");
        Session updatedSession = sessionRepository.save(savedSession);

        // Then
        assertThat(updatedSession.getName()).isEqualTo("updated session");
        assertThat(updatedSession.getDescription()).isEqualTo("updated description");
    }

    @Test
    public void testFindSessionById() {

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
        Optional<Session> retrievedSession = sessionRepository.findById(savedSession.getId());

        // Then
        assertThat(retrievedSession).isPresent();
        assertThat(retrievedSession.get().getId()).isEqualTo(savedSession.getId());
    }

    @Test
    public void testDeleteSession() {

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
        userRepository.delete(users.get(0));
        teacherRepository.delete(teacherSaved);
        sessionRepository.delete(savedSession);

        // Then
        assertThat(teacherRepository.findById(savedSession.getId())).isEmpty();
    }
}
