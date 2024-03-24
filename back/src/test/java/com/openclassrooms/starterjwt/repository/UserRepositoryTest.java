package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {

        // Given
        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void testFindById() {

        // Given
        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        User savedUser = userRepository.save(user);

        // When
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        // Then
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    public void testUpdateUser() {

        // Given
        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        User savedUser = userRepository.save(user);

        // When
        savedUser.setLastName("Leo");
        savedUser.setEmail("new-contact@test.com");
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertThat(updatedUser.getLastName()).isEqualTo("Leo");
        assertThat(updatedUser.getEmail()).isEqualTo("new-contact@test.com");
    }

    @Test
    public void testFindByEmail() {

        // Given
        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        User savedUser = userRepository.save(user);

        // When
        Optional<User> retrievedUser = userRepository.findByEmail(savedUser.getEmail());

        // Then
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(savedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {

        // Given
        User user = new User()
                .setFirstName("Alex")
                .setLastName("Test")
                .setAdmin(false)
                .setEmail("test@test.com")
                .setPassword("12345")
                .setCreatedAt(now())
                .setUpdatedAt(now());
        User savedUser = userRepository.save(user);

        // When
        userRepository.delete(savedUser);

        // Then
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }
}
