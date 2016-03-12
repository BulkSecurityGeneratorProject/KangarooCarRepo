package com.fexco.carshare.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.domain.Review;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.JourneyRepository;
import com.fexco.carshare.repository.ReviewRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.NotifyController;
import com.fexco.carshare.web.rest.dto.ReviewDTO;

@Service
@Transactional
public class ReviewService {
	
	@Inject
	UserRepository userRepository;
	
	@Inject
	ReviewRepository reviewRepository;
	
	@Inject
	JourneyRepository journeyRepository;
	
	@Inject
	NotifyController notifyController;
	
	public Review toEntity(ReviewDTO reviewDto){
		Review review = new Review();
		review.setReceiverUser(userRepository.findByLogin(reviewDto.getReceiverUserName()));
		review.setSenderUser(userRepository.findByLogin(reviewDto.getSenderUserName()));
		review.setMessage(reviewDto.getMessage());
		review.setValue(reviewDto.getValue());
		review.setJourney(journeyRepository.findOne(reviewDto.getJourneyId()));
		return review;
	}
	
	public ReviewDTO toDTO(Review review){
		ReviewDTO reviewDto = new ReviewDTO();
		reviewDto.setId(review.getId());
		reviewDto.setSenderUserName(review.getSenderUser().getLogin());
		reviewDto.setReceiverUserName(review.getReceiverUser().getLogin());
		reviewDto.setMessage(review.getMessage());
		reviewDto.setValue(review.getValue());
		reviewDto.setDate(review.getDate());
		reviewDto.setJourneyId(review.getJourney().getId());
		return reviewDto;
	}

	public ResponseEntity<?> addReview(ReviewDTO reviewDto) {
		if(reviewDto.getSenderUserName().equals(reviewDto.getReceiverUserName()))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		Review review = toEntity(reviewDto);
		Review savedReview = reviewRepository.save(review);
		String receiver = savedReview.getReceiverUser().getLogin();
		String[] link = {"profile", "username", receiver};
		Notification notification = new Notification(savedReview.getSenderUser().getLogin(), receiver, 
				" has added a review to your profile", link, "review", savedReview.getId());
		notifyController.sendNotification(receiver, notification);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	public Page<ReviewDTO> getReviewsForUser(String userName, Pageable page) {
		User user = userRepository.findByLogin(userName);
		Page<Review> reviews = reviewRepository.findAllByReceiverUser(user, page);
		Page<ReviewDTO> reviewDtos = new PageImpl<ReviewDTO>(convertToCommentPage(reviews.getContent()),page, reviews.getTotalElements());
		return reviewDtos;
	}

	private List<ReviewDTO> convertToCommentPage(List<Review> content) {
		List<ReviewDTO> dtos = new ArrayList<ReviewDTO>();
		for(Review review: content){
			dtos.add(toDTO(review));
		}
		return dtos;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getAverageAndAmountForUser(String userName) {
		int average = 0;
		User user = userRepository.findByLogin(userName);
		int numberOfReviews = reviewRepository.countByUser(user);
		if(numberOfReviews != 0){
			int sum = reviewRepository.sumValuesByUser(user);
			average = (int) Math.round((sum / (double)numberOfReviews));
		}
		JSONObject json = new JSONObject();
		json.put("average", average);
		json.put("number", numberOfReviews);
		return json;
	}

	public ReviewDTO getNewReview(Long reviewId) {
		Review review = reviewRepository.findOne(reviewId);
		return toDTO(review);
	}

	@SuppressWarnings("unchecked")
	public JSONObject hasReviewed(String senderUser, String receiverUser,
			Long journeyId) {
		User sender = userRepository.findByLogin(senderUser);
		User receiver = userRepository.findByLogin(receiverUser);
		Journey journey = journeyRepository.findOne(journeyId);
		Review review = reviewRepository.hasReview(sender, receiver, journey);
		boolean hasReview = review == null ? false: true;
		JSONObject json = new JSONObject();
		json.put("hasReview", hasReview);
		return json;
	}

}
