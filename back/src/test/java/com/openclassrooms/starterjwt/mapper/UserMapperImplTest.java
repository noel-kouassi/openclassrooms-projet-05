package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void testToEntity_WithValidDto_ReturnsEntity() {

        // Given
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("test@test.com");
        dto.setFirstName("Test");
        dto.setLastName("Test");
        dto.setPassword("password1234");
        dto.setAdmin(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        // When
        User entity = userMapper.toEntity(dto);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getPassword()).isEqualTo(dto.getPassword());
        assertThat(entity.isAdmin()).isEqualTo(dto.isAdmin());
        assertThat(entity.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    public void testToEntity_WithNullDto_ReturnsNull() {

        // When
        User entity = userMapper.toEntity((UserDto) null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    public void testToDto_WithValidEntity_ReturnsDto() {

        // Given
        User entity = new User();
        entity.setId(1L);
        entity.setEmail("test@test.com");
        entity.setFirstName("Test");
        entity.setLastName("Test");
        entity.setPassword("password1234");
        entity.setAdmin(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // When
        UserDto dto = userMapper.toDto(entity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getEmail()).isEqualTo(entity.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getPassword()).isEqualTo(entity.getPassword());
        assertThat(dto.isAdmin()).isEqualTo(entity.isAdmin());
        assertThat(dto.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    public void testToDto_WithNullEntity_ReturnsNull() {

        // When
        UserDto dto = userMapper.toDto((User) null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    public void testToEntityList_WithValidDtoList_ReturnsEntityList() {

        // Given
        List<UserDto> dtoList = new ArrayList<>();
        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("test1@test.com");
        dto1.setFirstName("Test");
        dto1.setLastName("Test");
        dto1.setPassword("password1");
        dto1.setAdmin(true);
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("test2@test.com");
        dto2.setFirstName("Test 2");
        dto2.setLastName("Test 2");
        dto2.setPassword("password2");
        dto2.setAdmin(false);
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());
        dtoList.add(dto1);
        dtoList.add(dto2);

        // When
        List<User> entityList = userMapper.toEntity(dtoList);

        // Then
        assertThat(entityList).isNotNull();
        assertThat(entityList).hasSize(2);

        assertThat(entityList.get(0).getId()).isEqualTo(dto1.getId());
        assertThat(entityList.get(0).getEmail()).isEqualTo(dto1.getEmail());
        assertThat(entityList.get(0).getFirstName()).isEqualTo(dto1.getFirstName());
        assertThat(entityList.get(0).getLastName()).isEqualTo(dto1.getLastName());
        assertThat(entityList.get(0).getPassword()).isEqualTo(dto1.getPassword());
        assertThat(entityList.get(0).isAdmin()).isEqualTo(dto1.isAdmin());
        assertThat(entityList.get(0).getCreatedAt()).isEqualTo(dto1.getCreatedAt());
        assertThat(entityList.get(0).getUpdatedAt()).isEqualTo(dto1.getUpdatedAt());

        assertThat(entityList.get(1).getId()).isEqualTo(dto2.getId());
        assertThat(entityList.get(1).getEmail()).isEqualTo(dto2.getEmail());
        assertThat(entityList.get(1).getFirstName()).isEqualTo(dto2.getFirstName());
        assertThat(entityList.get(1).getLastName()).isEqualTo(dto2.getLastName());
        assertThat(entityList.get(1).getPassword()).isEqualTo(dto2.getPassword());
        assertThat(entityList.get(1).isAdmin()).isEqualTo(dto2.isAdmin());
        assertThat(entityList.get(1).getCreatedAt()).isEqualTo(dto2.getCreatedAt());
        assertThat(entityList.get(1).getUpdatedAt()).isEqualTo(dto2.getUpdatedAt());
    }

    @Test
    public void testToEntityList_WithNullDtoList_ReturnsNull() {

        // When
        List<User> entityList = userMapper.toEntity((List<UserDto>) null);

        // Then
        assertThat(entityList).isNull();
    }

    @Test
    public void testToDtoList_WithValidEntityList_ReturnsDtoList() {

        // Given
        List<User> entityList = new ArrayList<>();
        User entity1 = new User();
        entity1.setId(1L);
        entity1.setEmail("test1@test.com");
        entity1.setFirstName("Test");
        entity1.setLastName("Test");
        entity1.setPassword("password1");
        entity1.setAdmin(true);
        entity1.setCreatedAt(LocalDateTime.now());
        entity1.setUpdatedAt(LocalDateTime.now());

        User entity2 = new User();
        entity2.setId(2L);
        entity2.setEmail("test2@test.com");
        entity2.setFirstName("Test 2");
        entity2.setLastName("Test 2");
        entity2.setPassword("password2");
        entity2.setAdmin(false);
        entity2.setCreatedAt(LocalDateTime.now());
        entity2.setUpdatedAt(LocalDateTime.now());
        entityList.add(entity1);
        entityList.add(entity2);

        // When
        List<UserDto> dtoList = userMapper.toDto(entityList);

        // Then
        assertThat(dtoList).isNotNull();
        assertThat(dtoList).hasSize(2);

        assertThat(dtoList.get(0).getId()).isEqualTo(entity1.getId());
        assertThat(dtoList.get(0).getEmail()).isEqualTo(entity1.getEmail());
        assertThat(dtoList.get(0).getFirstName()).isEqualTo(entity1.getFirstName());
        assertThat(dtoList.get(0).getLastName()).isEqualTo(entity1.getLastName());
        assertThat(dtoList.get(0).getPassword()).isEqualTo(entity1.getPassword());
        assertThat(dtoList.get(0).isAdmin()).isEqualTo(entity1.isAdmin());
        assertThat(dtoList.get(0).getCreatedAt()).isEqualTo(entity1.getCreatedAt());
        assertThat(dtoList.get(0).getUpdatedAt()).isEqualTo(entity1.getUpdatedAt());

        assertThat(dtoList.get(1).getId()).isEqualTo(entity2.getId());
        assertThat(dtoList.get(1).getEmail()).isEqualTo(entity2.getEmail());
        assertThat(dtoList.get(1).getFirstName()).isEqualTo(entity2.getFirstName());
        assertThat(dtoList.get(1).getLastName()).isEqualTo(entity2.getLastName());
        assertThat(dtoList.get(1).getPassword()).isEqualTo(entity2.getPassword());
        assertThat(dtoList.get(1).isAdmin()).isEqualTo(entity2.isAdmin());
        assertThat(dtoList.get(1).getCreatedAt()).isEqualTo(entity2.getCreatedAt());
        assertThat(dtoList.get(1).getUpdatedAt()).isEqualTo(entity2.getUpdatedAt());
    }

    @Test
    public void testToDtoList_WithNullEntityList_ReturnsNull() {

        // When
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);

        // Then
        assertThat(dtoList).isNull();
    }
}
