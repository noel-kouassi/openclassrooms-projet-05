package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SessionMapperImplTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Test
    public void testToEntityList_NullList() {

        // When
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToEntityList_EmptyList() {

        // When
        List<Session> result = sessionMapper.toEntity(Collections.emptyList());

        // Then
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testToDtoList_NullList() {

        // When
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToDtoList_EmptyList() {

        // When
        List<SessionDto> result = sessionMapper.toDto(Collections.emptyList());

        // Then
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testToEntity_NullDto() {

        // When
        Session result = sessionMapper.toEntity((SessionDto) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToDto_NullEntity() {

        // When
        SessionDto result = sessionMapper.toDto((Session) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToDto() {

        // Given
        Session session = new Session();
        session.setId(1L);
        session.setName("Test name");
        session.setDescription("Test description");

        // When
        SessionDto result = sessionMapper.toDto(session);

        // Then
        assertThat(session.getId()).isEqualTo(result.getId());
        assertThat(session.getName()).isEqualTo(result.getName());
        assertThat(session.getDescription()).isEqualTo(result.getDescription());
        assertThat(session.getDate()).isEqualTo(result.getDate());
    }

    @Test
    public void testToEntity() {

        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test name");
        sessionDto.setDescription("Test description");

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertThat(sessionDto.getId()).isEqualTo(result.getId());
        assertThat(sessionDto.getName()).isEqualTo(result.getName());
        assertThat(sessionDto.getDescription()).isEqualTo(result.getDescription());
    }
}
