package com.foodgo.service;

import com.foodgo.Exception.ReviewException;
import com.foodgo.model.Review;
import com.foodgo.model.User;
import com.foodgo.request.ReviewRequest;

import java.util.List;

public interface ReviewSerive {
	
    public Review submitReview(ReviewRequest review,User user);
    public void deleteReview(Long reviewId) throws ReviewException;
    public double calculateAverageRating(List<Review> reviews);
}
