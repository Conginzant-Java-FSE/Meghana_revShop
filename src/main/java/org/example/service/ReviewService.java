package org.example.service;

import org.example.dao.ReviewDao;
import org.example.models.Review;

import java.util.List;

public class ReviewService {

    private final ReviewDao dao = new ReviewDao();

    public boolean addReview(Review r) {
        return dao.addReview(r);
    }

    public List<Review> productReviews(int productId) {
        return dao.getProductReviews(productId);
    }

    public List<Review> reviewsForSeller(int sellerId){
        return dao.reviewsBySeller(sellerId);
    }
}
