package com.fexco.carshare.web.rest;

import javax.inject.Inject;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.service.ReviewService;
import com.fexco.carshare.web.rest.dto.ReviewDTO;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Inject
	ReviewService reviewService;
	
	@RequestMapping(value="/addReview",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addComment(
			@RequestBody ReviewDTO reviewDto) {
		return reviewService.addReview(reviewDto);
	}
	
	@RequestMapping(value = "/getReviews/{userName}", method = RequestMethod.GET)
    Page<ReviewDTO> findJourney(@PathVariable("userName") String userName, Pageable page) {
		return reviewService.getReviewsForUser(userName, page);
    }
	
	@RequestMapping(value = "/getAverageAndAmount/{userName}", method = RequestMethod.GET)
    JSONObject getAverageAndAmount(@PathVariable("userName") String userName) {
		return reviewService.getAverageAndAmountForUser(userName);
    }
	
	@RequestMapping(value = "/getNewReview/{reviewId}", method = RequestMethod.GET)
    ReviewDTO getNewReview(@PathVariable("reviewId") Long reviewId) {
		return reviewService.getNewReview(reviewId);
    }
	
	@RequestMapping(value = "/madeReview/{senderUser}/{receiverUser}/{journeyId}", method = RequestMethod.GET)
	JSONObject hasReviewed(@PathVariable("senderUser") String senderUser, @PathVariable("receiverUser") String receiverUser, @PathVariable("journeyId") Long journeyId) {
		return reviewService.hasReviewed(senderUser, receiverUser, journeyId);
    }
}
