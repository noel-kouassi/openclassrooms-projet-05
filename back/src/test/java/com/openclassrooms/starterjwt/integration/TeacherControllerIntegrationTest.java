package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeacherControllerIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherController teacherController;

    @AfterEach
    public void cleanUp() {
        teacherRepository.deleteAll();
    }

    @Test
    public void testFindById() {

        // Given
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Alex");
        teacher.setUpdatedAt(now());
        teacher.setCreatedAt(now());

        Teacher teacherSaved = teacherRepository.save(teacher);

        // When
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(teacherSaved.getId()));

        // Then
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isInstanceOf(TeacherDto.class);

        TeacherDto teacherDto = (TeacherDto) responseEntity.getBody();
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacherSaved.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo("Test");
        assertThat(teacherDto.getLastName()).isEqualTo("Alex");
    }

    @Test
    public void testFindById_WhenTeacherNotFound() {

        // Given
        String id = "1";

        // When
        ResponseEntity<?> responseEntity = teacherController.findById(id);

        // Then
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void testFindAll() {

        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Test");
        teacher1.setLastName("Alex");
        teacher1.setUpdatedAt(now());
        teacher1.setCreatedAt(now());
        Teacher teacherSaved1 = teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Test 2");
        teacher2.setLastName("Alex 2");
        teacher2.setUpdatedAt(now());
        teacher2.setCreatedAt(now());
        Teacher teacherSaved2 = teacherRepository.save(teacher2);

        // When
        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Then
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isInstanceOf(List.class);

        List<TeacherDto> teacherDtos = (List<TeacherDto>) responseEntity.getBody();
        assertThat(teacherDtos).isNotEmpty();
        assertThat(teacherDtos.size()).isEqualTo(2);
        assertThat(teacherDtos.stream().map(TeacherDto::getId).collect(Collectors.toList())).containsOnly(teacherSaved1.getId(), teacherSaved2.getId());
    }
}
