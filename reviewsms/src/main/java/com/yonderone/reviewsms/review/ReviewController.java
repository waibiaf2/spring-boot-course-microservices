package com.yonderone.reviewsms.review;

import com.yonderone.reviewsms.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMessageProducer reviewMessageProducer;


    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(
        @RequestParam Long companyId
    ) {
        List<Review> reviews = reviewService.findAll(companyId);

        return new ResponseEntity<>(
            reviews,
            HttpStatus.OK
        );
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(
        @PathVariable Long reviewId
    ) {
        Review review = reviewService.getReviewById(reviewId);

        if (review != null) {
            return new ResponseEntity<>(
                review,
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping
    public ResponseEntity<String> addReview(
        @RequestParam Long companyId,
        @RequestBody Review review
    ) {
        boolean isReviewSaved = reviewService.addReview(companyId, review);
        if (isReviewSaved) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>(
                "Review added",
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                "Review Was Not Saved",
                HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
        @PathVariable Long reviewId,
        @RequestBody Review review
    ) {
        boolean reviewUpdated = reviewService.updateReview(reviewId, review);

        if (reviewUpdated) {
            return new ResponseEntity<>(
                "Review Successfully updates",
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                "Review Not Updated",
                HttpStatus.NOT_FOUND
            );
        }

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
        @PathVariable Long reviewId
    ) {
        boolean isReviewDeleted = reviewService.deleteReview(reviewId);

        if (isReviewDeleted) {
            return new ResponseEntity<>(
                "Review Deleted Successfully",
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                "Review Not Deleted",
                HttpStatus.NOT_FOUND
            );
        }
    }
}
