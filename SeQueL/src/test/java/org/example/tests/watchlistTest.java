package org.example.tests;

import org.example.repos.watchlistRepo;
import org.example.service.watchlistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class watchlistTest {

    @Mock
    private watchlistRepo watchlistRepo;
    
    @InjectMocks
    private watchlistService watchlistService;

    // tdd test check if movie is in watchlist
    // comment out inWatchlist()
    @Test
    void testInWatchlist() throws SQLException {
        // arrange
        int userID = 1;
        int movieID = 8;

        when(watchlistRepo.onList(userID, movieID)).thenReturn(true);

        // act
        boolean result = watchlistService.inWatchlist(userID, movieID);

        // assert
        assertTrue(result);
        verify(watchlistRepo).onList(userID, movieID);
    }

    // tdd test error handling
    @Test
    void exceptionSQL() throws SQLException {
        // arrange
        int userID = 1;
        int movieID = 8;

        when(watchlistRepo.onList(userID, movieID)).thenThrow(SQLException.class);

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> watchlistService.inWatchlist(userID, movieID));

        // assert
        assertTrue(exception.getMessage().contains("failed to check watchlist"));
    }
    
}
