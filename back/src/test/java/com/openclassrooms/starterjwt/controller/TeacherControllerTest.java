package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    public void testFindById() {

        // Given
        long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        when(teacherService.findById(id)).thenReturn(teacher);

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // When
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(id));

        // Then
        TeacherDto teacherDtoResponse = (TeacherDto) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teacherDtoResponse).isNotNull();
        assertThat(teacherDtoResponse.getId()).isEqualTo(1L);

        verify(teacherService, times(1)).findById(id);
        verify(teacherMapper, times(1)).toDto(teacher);
    }

    @Test
    public void testFindById_NotFound() {

        // Given
        long id = 1L;
        when(teacherService.findById(id)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(id));

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(teacherService, times(1)).findById(id);
    }

    @Test
    public void testFindById_InvalidId() {

        // Given
        String id = "badId";

        // When
        ResponseEntity<?> responseEntity = teacherController.findById(id);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(teacherService, never()).findById(anyLong());
    }

    @Test
    public void testFindAll() {

        // Given
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher1 = new Teacher()
                .setId(1L);
        Teacher teacher2 = new Teacher()
                .setId(2L);
        teachers.add(teacher1);
        teachers.add(teacher2);

        List<TeacherDto> teacherDtos = new ArrayList<>();
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDtos.add(teacherDto1);
        teacherDtos.add(teacherDto2);

        // When
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Then
        List<TeacherDto> teacherDtoResponse = (List<TeacherDto>) responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teacherDtoResponse).isNotEmpty();
        assertThat(teacherDtoResponse.size()).isEqualTo(teachers.size());

        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    }
}
