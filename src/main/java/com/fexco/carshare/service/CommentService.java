package com.fexco.carshare.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fexco.carshare.domain.Comment;
import com.fexco.carshare.domain.Journey;
import com.fexco.carshare.domain.JourneyComment;
import com.fexco.carshare.domain.Notification;
import com.fexco.carshare.domain.User;
import com.fexco.carshare.repository.CommentRepository;
import com.fexco.carshare.repository.JourneyCommentRepository;
import com.fexco.carshare.repository.JourneyRepository;
import com.fexco.carshare.repository.UserRepository;
import com.fexco.carshare.web.rest.NotifyController;
import com.fexco.carshare.web.rest.dto.CommentDTO;
import com.fexco.carshare.web.rest.dto.JourneyCommentDTO;

@Service
@Transactional
public class CommentService {
	
	@Inject
	UserRepository userRepository;
	
	@Inject
	CommentRepository commentRepository;
	
	@Inject
	JourneyCommentRepository journeyCommentRepository;
	
	@Inject
	JourneyRepository journeyRepository;
	
	@Inject
	NotifyController notifyController;
	
	public ResponseEntity<?> addComment(CommentDTO commentDto){
		Comment comment = toEntity(commentDto);
		Comment savedComment = commentRepository.save(comment);
		String receiverName = comment.getReceiverUser().getLogin();
		Notification notification =null;
		String[] link = {"profile", "username", receiverName};
		if(comment.isReply()){
			notification = new Notification(comment.getSenderUser().getLogin(), receiverName, 
					" has replied to a comment on your profile", link, "reply", savedComment.getId());
			String originalCommenter = comment.getComment().getSenderUser().getLogin();
			Notification toOriginalCommenter = new Notification(comment.getSenderUser().getLogin(), 
					originalCommenter, " has replied to your comment", link, "reply", savedComment.getId());
			notifyController.sendNotification(originalCommenter, toOriginalCommenter);
		}
		else{
			notification = new Notification(comment.getSenderUser().getLogin(), receiverName, 
					" has commented on your profile", link, "comment", savedComment.getId());
		}
		notifyController.sendNotification(receiverName, notification);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> addJourneyComment(JourneyCommentDTO journeyCommentDto){
		JourneyComment journeyComment = toEntity(journeyCommentDto);
		JourneyComment savedJourneyComment =  journeyCommentRepository.save(journeyComment);
		String receiverName = journeyComment.getJourney().getUser().getLogin();
		Long journeyId = journeyComment.getJourney().getId();
		Notification notification =null;
		String[] link = {"journey", "journeyId", journeyId.toString()};
		if(journeyComment.isReply()){
			notification = new Notification(journeyComment.getSenderUser().getLogin(), receiverName, 
					" has replied to a comment on one of your journeys", link, "journeyReply", savedJourneyComment.getId());
			String originalCommenter = journeyComment.getComment().getSenderUser().getLogin();
			Notification toOriginalCommenter = new Notification(journeyComment.getSenderUser().getLogin(), 
					originalCommenter, " has replied to your comment", link, "journeyReply", savedJourneyComment.getId());
			notifyController.sendNotification(originalCommenter, toOriginalCommenter);
		}
		else{
			notification = new Notification(journeyComment.getSenderUser().getLogin(), receiverName, 
					" has commented on one of your journeys", link, "journeyComment", savedJourneyComment.getId());
		}
		notifyController.sendNotification(receiverName, notification);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	public Page<CommentDTO> getCommentsForUser(String userName, Pageable page){
		User user = userRepository.findByLogin(userName);
		Page<Comment> comments = commentRepository.findAllByReceiverUser(user, page);
		Page<CommentDTO> commentDtos = new PageImpl<CommentDTO>(convertToCommentPage(comments.getContent()),page, comments.getTotalElements());
		return commentDtos;
	}
	
	public Page<JourneyCommentDTO> getCommentsForJourney(Long journeyId, Pageable page){
		Journey journey = journeyRepository.findOne(journeyId);
		Page<JourneyComment> journeyComments = journeyCommentRepository.findAllByJourneyId(journey, page);
		Page<JourneyCommentDTO> journeyCommentDtos = new PageImpl<JourneyCommentDTO>(convertToJourneyCommentPage(journeyComments.getContent()),
				page, journeyComments.getTotalElements());
		return journeyCommentDtos;
	}
	
	private List<JourneyCommentDTO> convertToJourneyCommentPage(List<JourneyComment> content) {
		List<JourneyCommentDTO> dtos = new ArrayList<JourneyCommentDTO>();
		for(JourneyComment journeyComment: content){
			dtos.add(toDTO(journeyComment));
		}
		return dtos;
	}

	public List<CommentDTO> convertToCommentPage(List<Comment> content) {
		List<CommentDTO> dtos = new ArrayList<CommentDTO>();
		for(Comment comment: content){
			dtos.add(toDTO(comment));
		}
		return dtos;
	}
	
	public Comment toEntity(CommentDTO commentDto){
		Comment comment = new Comment();
		comment.setSenderUser(userRepository.findByLogin(commentDto.getSenderUserName()));
		comment.setReceiverUser(userRepository.findByLogin(commentDto.getReceiverUserName()));
		comment.setMessage(commentDto.getMessage());
		comment.setReply(commentDto.isReply());
		if(comment.isReply()){
			comment.setComment(commentRepository.findOne(commentDto.getCommentId()));
		}
		return comment;
	}
	
	private JourneyComment toEntity(JourneyCommentDTO journeyCommentDto) {
		JourneyComment journeyComment = new JourneyComment();
		journeyComment.setSenderUser(userRepository.findByLogin(journeyCommentDto.getSenderUserName()));
		journeyComment.setJourney(journeyRepository.findOne(journeyCommentDto.getJourneyId()));
		journeyComment.setMessage(journeyCommentDto.getMessage());
		journeyComment.setReply(journeyCommentDto.isReply());
		if(journeyComment.isReply()){
			journeyComment.setComment(journeyCommentRepository.findOne(journeyCommentDto.getCommentId()));
		}
		return journeyComment;
	}
	
	public CommentDTO toDTO(Comment comment){
		CommentDTO commentDto = new CommentDTO();
		commentDto.setId(comment.getId());
		commentDto.setSenderUserName(comment.getSenderUser().getLogin());
		commentDto.setReceiverUserName(comment.getReceiverUser().getLogin());
		commentDto.setMessage(comment.getMessage());
		commentDto.setReply(comment.isReply());
		if(comment.getComment()!=null){
			commentDto.setCommentId(comment.getComment().getId());
		}
		commentDto.setDate(comment.getDate());
		return commentDto;
	}
	
	private JourneyCommentDTO toDTO(JourneyComment journeyComment) {
		JourneyCommentDTO commentDto = new JourneyCommentDTO();
		commentDto.setId(journeyComment.getId());
		commentDto.setSenderUserName(journeyComment.getSenderUser().getLogin());
		commentDto.setJourneyId(journeyComment.getJourney().getId());
		commentDto.setMessage(journeyComment.getMessage());
		commentDto.setReply(journeyComment.isReply());
		if(journeyComment.getComment()!=null){
			commentDto.setCommentId(journeyComment.getComment().getId());
		}
		commentDto.setDate(journeyComment.getDate());
		return commentDto;
	}

	public Page<CommentDTO> getRepliesForComment(Long commentId, Pageable page) {
		Comment comment = commentRepository.findOne(commentId);
		Page<Comment> comments = commentRepository.findAllByComment(comment, page);
		Page<CommentDTO> commentDtos = new PageImpl<CommentDTO>(convertToCommentPage(comments.getContent()),
				page, comments.getTotalElements());
		return commentDtos;
	}

	public Page<JourneyCommentDTO> getRepliesForJourneyComment(Long journeyCommentId, Pageable page){
		JourneyComment journeyComment = journeyCommentRepository.findOne(journeyCommentId);
		Page<JourneyComment> journeyComments = journeyCommentRepository.findAllByComment(journeyComment, page);
		Page<JourneyCommentDTO> journeyCommentDtos = new PageImpl<JourneyCommentDTO>(convertToJourneyCommentPage(journeyComments.getContent()),
				page, journeyComments.getTotalElements());
		return journeyCommentDtos;
	}

	public CommentDTO getNewComment(Long commentId) {
		Comment comment = commentRepository.findOne(commentId);
		return toDTO(comment);
	}

}
