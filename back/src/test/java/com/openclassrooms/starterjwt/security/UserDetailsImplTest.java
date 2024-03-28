package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("Test")
                .lastName("Test")
                .admin(false)
                .password("password")
                .build();
    }

    @Test
    public void testUserDetailsConstructor() {
        assertThat(userDetails.getId()).isEqualTo(1L);
        assertThat(userDetails.getUsername()).isEqualTo("testUser");
        assertThat(userDetails.getFirstName()).isEqualTo("Test");
        assertThat(userDetails.getLastName()).isEqualTo("Test");
        assertThat(userDetails.getAdmin()).isFalse();
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }

    @Test
    public void testIsEnabled() {
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    public void testIsAccountNonExpired() {
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    public void testIsAccountNonLocked() {
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void testEquals() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userDetails3 = UserDetailsImpl.builder().id(2L).build();
        assertThat(userDetails).isEqualTo(userDetails1).isEqualTo(userDetails2).isNotEqualTo(userDetails3);
    }
}
