package org.example.tests;

import org.example.models.user;
import org.example.repos.userRepo;
import org.example.service.userService;
import org.example.repos.reviewRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class userTest {

    @Mock
    private userRepo userRepo;

    @Mock
    private reviewRepo reviewRepo;

    @InjectMocks
    private userService userService;

    private user tester;

    @BeforeEach
    public void setUp() {
        tester = new user("testuser");
    }

    // happy path register user
    @Test
    void registerUser() throws SQLException {
        // arrange
        String username = "kenneth";

        when(userRepo.usernameExists(username)).thenReturn(false);

        // act
        user result = userService.register(username);

        // assert
        assertNotNull(result);
        assertEquals(tester.getUserID(), result.getUserID());
        verify(userRepo).readName(tester.getUsername()).isPresent();
    }

    
}

