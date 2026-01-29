package org.example.controller;

import org.example.models.Review;
import org.example.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.*;

class ReviewControllerTest {

    private ReviewController controller;
    private ReviewService reviewService;

    @BeforeEach
    void setup() throws Exception {
        controller = new ReviewController();
        reviewService = mock(ReviewService.class);

        Field f = ReviewController.class.getDeclaredField("reviewService");
        f.setAccessible(true);
        f.set(controller, reviewService);
    }

    @Test
    void showProductReviews_noReviews() {
        when(reviewService.productReviews(10)).thenReturn(List.of());

        controller.showProductReviews(10);

        verify(reviewService).productReviews(10);
    }

    @Test
    void showProductReviews_withReviews() {
        Review r = new Review();
        r.setBuyerName("User");
        r.setRating(5);
        r.setComment("Nice");

        when(reviewService.productReviews(10))
                .thenReturn(List.of(r));

        controller.showProductReviews(10);

        verify(reviewService).productReviews(10);
    }
}
