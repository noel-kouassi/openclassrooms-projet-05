package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Test
    public void testDoFilterInternalWithValidToken() throws ServletException, IOException {

        // Given
        String email = "test@test.com";
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "Alex", "Test", false, "1234");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // When
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("test@test.com");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testParseJwtWhenHeaderIsPresentAndValid() {

        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // When
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        String token = authTokenFilter.parseJwt(request);

        // Then
        assertEquals("validToken", token);
    }

    @Test
    public void testParseJwtWhenHeaderIsNotPresent() {

        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // When
        String token = authTokenFilter.parseJwt(request);

        // Then
        assertNull(token);
    }

    @Test
    public void testParseJwtWhenHeaderIsEmpty() {

        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // When
        when(request.getHeader("Authorization")).thenReturn("");

        String token = authTokenFilter.parseJwt(request);

        // Then
        assertNull(token);
    }
}
