package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TeacherMapperImplTest {

    private TeacherMapperImpl teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    public void testToEntity_WithValidDto_ReturnsEntity() {

        // Given
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("Test");
        dto.setLastName("Test");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // When
        Teacher entity = teacherMapper.toEntity(dto);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    public void testToEntity_WithNullDto_ReturnsNull() {

        // When
        Teacher entity = teacherMapper.toEntity((TeacherDto) null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    public void testToDto_WithValidEntity_ReturnsDto() {

        // Given
        Teacher entity = new Teacher();
        entity.setId(1L);
        entity.setFirstName("Test");
        entity.setLastName("Test");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        TeacherDto dto = teacherMapper.toDto(entity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    public void testToDto_WithNullEntity_ReturnsNull() {

        // When
        TeacherDto dto = teacherMapper.toDto((Teacher) null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    public void testToEntityList_WithValidDtoList_ReturnsEntityList() {

        // Given
        List<TeacherDto> dtoList = new ArrayList<>();
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("Test");
        dto1.setLastName("Test");
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Test 1");
        dto2.setLastName("Test 1");
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());
        dtoList.add(dto1);
        dtoList.add(dto2);

        // When
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        // Then
        assertThat(entityList).isNotNull();
        assertThat(entityList).hasSize(2);

        assertThat(entityList.get(0).getId()).isEqualTo(dto1.getId());
        assertThat(entityList.get(0).getFirstName()).isEqualTo(dto1.getFirstName());
        assertThat(entityList.get(0).getLastName()).isEqualTo(dto1.getLastName());
        assertThat(entityList.get(0).getCreatedAt()).isEqualTo(dto1.getCreatedAt());
        assertThat(entityList.get(0).getUpdatedAt()).isEqualTo(dto1.getUpdatedAt());

        assertThat(entityList.get(1).getId()).isEqualTo(dto2.getId());
        assertThat(entityList.get(1).getFirstName()).isEqualTo(dto2.getFirstName());
        assertThat(entityList.get(1).getLastName()).isEqualTo(dto2.getLastName());
        assertThat(entityList.get(1).getCreatedAt()).isEqualTo(dto2.getCreatedAt());
        assertThat(entityList.get(1).getUpdatedAt()).isEqualTo(dto2.getUpdatedAt());
    }

    @Test
    public void testToEntityList_WithNullDtoList_ReturnsNull() {

        // When
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertThat(entityList).isNull();
    }

    @Test
    public void testToDtoList_WithValidEntityList_ReturnsDtoList() {

        // Given
        List<Teacher> entityList = new ArrayList<>();
        Teacher entity1 = new Teacher();
        entity1.setId(1L);
        entity1.setFirstName("Test");
        entity1.setLastName("Test");
        entity1.setCreatedAt(LocalDateTime.now());
        entity1.setUpdatedAt(LocalDateTime.now());

        Teacher entity2 = new Teacher();
        entity2.setId(2L);
        entity2.setFirstName("Test 1");
        entity2.setLastName("Test 1");
        entity2.setCreatedAt(LocalDateTime.now());
        entity2.setUpdatedAt(LocalDateTime.now());
        entityList.add(entity1);
        entityList.add(entity2);

        // When
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Then
        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);

        assertThat(dtoList.get(0).getId()).isEqualTo(entity1.getId());
        assertThat(dtoList.get(0).getFirstName()).isEqualTo(entity1.getFirstName());
        assertThat(dtoList.get(0).getLastName()).isEqualTo(entity1.getLastName());
        assertThat(dtoList.get(0).getCreatedAt()).isEqualTo(entity1.getCreatedAt());
        assertThat(dtoList.get(0).getUpdatedAt()).isEqualTo(entity1.getUpdatedAt());

        assertThat(dtoList.get(1).getId()).isEqualTo(entity2.getId());
        assertThat(dtoList.get(1).getFirstName()).isEqualTo(entity2.getFirstName());
        assertThat(dtoList.get(1).getLastName()).isEqualTo(entity2.getLastName());
        assertThat(dtoList.get(1).getCreatedAt()).isEqualTo(entity2.getCreatedAt());
        assertThat(dtoList.get(1).getUpdatedAt()).isEqualTo(entity2.getUpdatedAt());
    }

    @Test
    public void testToDtoList_WithNullEntityList_ReturnsNull() {

        // When
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertThat(dtoList).isNull();
    }
}
