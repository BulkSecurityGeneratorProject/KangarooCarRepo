package com.fexco.carshare.web.rest;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fexco.carshare.service.CommentService;
import com.fexco.carshare.web.rest.dto.CommentDTO;
import com.fexco.carshare.web.rest.dto.JourneyCommentDTO;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	@Inject
	CommentService commentService;
	
	@RequestMapping(value="/addComment",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addComment(
			@RequestBody CommentDTO commentDto) {
		return commentService.addComment(commentDto);
	}
	
	@RequestMapping(value="/addJourneyComment",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addJourneyComment(
			@RequestBody JourneyCommentDTO journeyCommentDto) {
		return commentService.addJourneyComment(journeyCommentDto);
	}
	
	@RequestMapping(value = "/getComments/{userName}", method = RequestMethod.GET)
    Page<CommentDTO> findComments(@PathVariable("userName") String userName, Pageable page) {
		return commentService.getCommentsForUser(userName, page);
    }
	
	@RequestMapping(value = "/getJourneyComments/{journeyId}", method = RequestMethod.GET)
    Page<JourneyCommentDTO> findJourneyComments(@PathVariable("journeyId") Long journeyId, Pageable page) {
		return commentService.getCommentsForJourney(journeyId, page);
    }
	
	@RequestMapping(value = "/getReplies/{commentId}", method = RequestMethod.GET)
	Page<CommentDTO> findReplies(@PathVariable("commentId") Long commentId, Pageable page) {
		return commentService.getRepliesForComment(commentId, page);
    }
	
	@RequestMapping(value = "/getJourneyReplies/{commentId}", method = RequestMethod.GET)
	Page<JourneyCommentDTO> findJourneyReplies(@PathVariable("commentId") Long commentId, Pageable page) {
		return commentService.getRepliesForJourneyComment(commentId, page);
    }
	
	@RequestMapping(value = "/getNewComment/{commentId}", method = RequestMethod.GET)
	CommentDTO findNewComment(@PathVariable("commentId") Long commentId){
		return commentService.getNewComment(commentId);
	}
}
