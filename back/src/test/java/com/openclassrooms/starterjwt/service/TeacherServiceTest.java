package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testFindAllTeachers() {

        // Given
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        teachers.add(new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        // When
        List<Teacher> foundTeachers = teacherService.findAll();

        // Then
        assertThat(foundTeachers.size()).isEqualTo(2);
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindTeacherByIdWhenExists() {

        // Given
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // When
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Then
        assertThat(teacherId).isEqualTo(foundTeacher.getId());
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    public void testFindTeacherByIdWhenNotExists() {

        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Then
        assertThat(foundTeacher).isNull();
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
