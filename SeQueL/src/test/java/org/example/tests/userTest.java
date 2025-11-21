package org.example.tests;

import org.example.models.user;
import org.example.repos.userRepo;
import org.example.service.userService;
import org.example.repos.reviewRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class userTest {

    @Mock
    private userRepo userRepo;

    @Mock
    private reviewRepo reviewRepo;

    @InjectMocks
    private userService userService;

    // happy path register user
    @Test
    void registerUser() throws SQLException {
        // arrange
        String username = "kenneth";
        user expectedUser = new user(username);

        when(userRepo.usernameExists(username)).thenReturn(false);
        when(userRepo.create(any(user.class))).thenReturn(expectedUser);

        // act
        user result = userService.register(username);

        // assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepo).usernameExists(username);
        verify(userRepo).create(any(user.class));
    }

    // negative path duplicate registeration
    @Test
    void duplicateRegistration() throws SQLException {
        // arrange
        String username = "kenneth";

        when(userRepo.usernameExists(username)).thenReturn(true);

        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(username));

        // assert
        assertEquals("username already exists", exception.getMessage());
        verify(userRepo).usernameExists(username);
    }

    // negative path login non-existent user
    @Test
    void loginNonExistentUser() throws SQLException {
        // arrange
        String username = "kenneth";

        when(userRepo.readName(username)).thenReturn(Optional.empty());

        // act
        Optional<user> result = userService.login(username);

        // assert
        assertFalse(result.isPresent());
        verify(userRepo).readName(username);
    }

    // edge case register with min valid uesrname length
    @Test
    void minValid() throws SQLException {
        // arrange
        String username = "ken";
        user expectedUser = new user(username);

        when(userRepo.usernameExists(username)).thenReturn(false);
        when(userRepo.create(any(user.class))).thenReturn(expectedUser);

        // act
        user result = userService.register(username);

        // assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepo).usernameExists(username);
        verify(userRepo).create(any(user.class));
    }

    // edge case username too short
    @Test
    void tooShort() throws SQLException {
        // arrange
        String username = "ke";
        
        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.register(username));

        // assert
        assertTrue(exception.getMessage().contains("username must be in the bounds"));
        // validateName is called first and throws before usernameExists is called
        verify(userRepo, never()).usernameExists(username);
    }

}