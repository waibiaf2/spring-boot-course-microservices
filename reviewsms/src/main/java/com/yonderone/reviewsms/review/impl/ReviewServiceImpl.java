package com.yonderone.reviewsms.review.impl;


import com.yonderone.reviewsms.review.Review;
import com.yonderone.reviewsms.review.ReviewRepository;
import com.yonderone.reviewsms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(
        ReviewRepository reviewRepository
    ) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public Boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public Boolean updateReview(Long reviewId, Review review) {
        Review reviewToUpdate = getReviewById(reviewId);

        if (reviewToUpdate != null) {
            reviewToUpdate.setCompanyId(review.getCompanyId());
            reviewToUpdate.setTitle(review.getTitle());
            reviewToUpdate.setDescription(review.getDescription());
            reviewToUpdate.setRating(review.getRating());

            reviewRepository.save(reviewToUpdate);
            return true;
        }

        return false;
    }

    @Override
    public Boolean deleteReview(Long reviewId) {
        boolean reviewExists = reviewRepository.existsById(reviewId);

        if (reviewExists) {
            reviewRepository.deleteById(reviewId);
            return true;
        } else {
            return false;
        }
    }
}
