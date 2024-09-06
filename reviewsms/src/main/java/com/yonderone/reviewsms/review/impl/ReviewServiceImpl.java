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

    public List<Review> findAll(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review review) {
        Review reviewToUpdate = reviewRepository.findById(reviewId).orElse(null);

        if (reviewToUpdate != null) {
            reviewToUpdate.setCompanyId(review.getCompanyId());
            reviewToUpdate.setTitle(review.getTitle());
            reviewToUpdate.setDescription(review.getDescription());
            reviewToUpdate.setAuthor(review.getAuthor());
            reviewToUpdate.setRating(review.getRating());

            reviewRepository.save(reviewToUpdate);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        boolean reviewExists = reviewRepository.existsById(reviewId);

        if (reviewExists) {
            Review review = reviewRepository.findById(reviewId).orElse(null);
            reviewRepository.deleteById(reviewId);
            return true;
        } else {
            return false;
        }
    }
}
