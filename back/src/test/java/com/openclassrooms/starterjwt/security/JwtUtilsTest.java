package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(1L, "test", "test", "test", true, "1234"));
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    public void generateJwtToken_ValidAuthentication_GeneratesToken() {

        // Given
        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(1L, "testpassword", "test", "test", true, "1234"));

        // When
        String token = jwtUtils.generateJwtToken(authentication);

        // Then
        assertThat(token).isNotNull().isNotEmpty().contains(".");
    }

    @Test
    public void getUserNameFromJwtToken_ValidToken_ReturnsUsername() {

        // Given
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Then
        assertThat(username).isEqualTo("test");
    }

    @Test
    public void validateJwtToken_ValidToken_ReturnsTrue() {

        // Given
        String token = jwtUtils.generateJwtToken(authentication);

        // When
        boolean isTokenValid = jwtUtils.validateJwtToken(token);

        // Then
        assertThat(isTokenValid).isTrue();
    }

    @Test
    public void validateJwtToken_EmptyToken_ThrowsIllegalArgumentException() {

        // Given
        String emptyToken = "klkl25554.mlpml.mlkm";

        // When
        boolean isTokenValid = jwtUtils.validateJwtToken(emptyToken);

        // Then
        assertThat(isTokenValid).isFalse();
    }
}
