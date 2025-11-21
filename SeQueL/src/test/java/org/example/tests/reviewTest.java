package org.example.tests;

import org.example.models.user;
import org.example.models.movie;
import org.example.models.review;
import org.example.repos.userRepo;
import org.example.repos.movieRepo;
import org.example.repos.reviewRepo;
import org.example.service.reviewService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class reviewTest {

    @Mock
    private reviewRepo reviewRepo;

    @Mock
    private movieRepo movieRepo;

    @Mock
    private userRepo userRepo;

    @InjectMocks
    private reviewService reviewService;

    private user tester;
    private movie testMovie;
    private review testReview;

    @BeforeEach
    void setUp() {
        tester = new user(1,"testuser", LocalDateTime.now());
        testMovie = new movie(1,"testmovie", LocalDate.now(), "testdirector", 120, "testoverview");
        testReview = new review(1,1,5,"testreview", LocalDate.now());
    }

    // happy path create review
    @Test
    void testCreateReview() throws SQLException {
        // arrange
        int userID = 1;
        int movieID = 1;
        double rating = 5;
        String review = "testreview";
        LocalDate watchDate = LocalDate.now();

        when(userRepo.readID(userID)).thenReturn(Optional.of(tester));
        when(movieRepo.readID(movieID)).thenReturn(Optional.of(testMovie));
        when(reviewRepo.findUserMovie(userID, movieID)).thenReturn(Optional.empty());
        when(reviewRepo.create(any(review.class))).thenReturn(testReview);

        // act
        review result = reviewService.create(userID, movieID, rating, review, watchDate);

        // assert
        assertEquals(testReview, result);
        verify(reviewRepo).create(any(review.class));
    }

    // happy path many to many query
    @Test
    void manyToMany() throws SQLException {
        // arrange
        int userID = 1;
        List<review> reviews = Arrays.asList(testReview);

        when(reviewRepo.findUser(userID)).thenReturn(reviews);

        // act
        List<review> result = reviewService.getUserReviews(userID);

        // assert
        assertEquals(testReview.getReviewID(), result.get(0).getReviewID());
        verify(reviewRepo).findUser(userID);
    
}
