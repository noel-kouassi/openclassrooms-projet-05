package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void testSaveTeacher() {

        // Given
        Teacher teacher = new Teacher()
                .setLastName("Test")
                .setFirstName("Alex")
                .setCreatedAt(now())
                .setUpdatedAt(now());

        // When
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Then
        assertThat(savedTeacher.getId()).isNotNull();
    }

    @Test
    public void testFindTeacherById() {

        // Given
        Teacher teacher = new Teacher()
                .setLastName("Test")
                .setFirstName("Alex")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        Teacher savedTeacher = teacherRepository.save(teacher);

        // When
        Optional<Teacher> retrievedTeacher = teacherRepository.findById(savedTeacher.getId());

        // Then
        assertThat(retrievedTeacher).isPresent();
        assertThat(retrievedTeacher.get().getId()).isEqualTo(savedTeacher.getId());
    }

    @Test
    public void testUpdateTeacher() {

        // Given
        Teacher teacher = new Teacher()
                .setLastName("Test")
                .setFirstName("Alex")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        Teacher savedTeacher = teacherRepository.save(teacher);

        // When
        savedTeacher.setLastName("Leo");
        Teacher updatedTeacher = teacherRepository.save(savedTeacher);

        // Then
        assertThat(updatedTeacher.getLastName()).isEqualTo("Leo");
    }

    @Test
    public void testDeleteTeacher() {

        // Given
        Teacher teacher = new Teacher()
                .setLastName("Test")
                .setFirstName("Alex")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        Teacher savedTeacher = teacherRepository.save(teacher);

        // When
        teacherRepository.delete(savedTeacher);

        // Then
        assertThat(teacherRepository.findById(savedTeacher.getId())).isEmpty();
    }
}

