'use strict';

angular.module('carshareApp')
    .controller('ProfileController', function ($scope, $rootScope, $stateParams, Principal, $translate, $timeout, Auth, $http, $state, $location) {
    	$scope.go = function (path) {
            $location.path(path);
        };
        $scope.url = window.location.href.toString().split(window.location.host)[1];
        $scope.singlePageJourneys = false;
        $scope.singlePageJourneyRequests = false;
        $scope.singlePageBids = false;        
        $scope.statusOne = true;
        $scope.statusTwo = false;
    	$scope.statusThree = false;
        var vm = this;
  		vm.time = new Date();
  		$scope.currentCommentPage = 0;
        $scope.commentError = null;
        $scope.more = false;
        $scope.profileUserName = $stateParams.username;
        $scope.bio = $scope.profileUserName + " has not added a bio yet";
        $scope.bioInput;
        $scope.showComments = true;
        $scope.getUser = function(){
        	$http.get('api/users/'+$scope.profileUserName).
        	success(function(data, status){
        	}).
        	error(function(data, status){
        		$state.go('error');
        	});        	
        }; 
        
        $scope.updateProfile = function(){
        	var profileData ={
        		'userName': $scope.profileUserName,
        		'bio': $scope.profile.bio
        	};
        	$http.post('/api/myProfile/addProfile',profileData).
        	success(function(data, status, headers, config){
        		$state.reload();
        	});
        };
        
        $scope.postComment = function(){
        	var commentData = {
        		'senderUserName': $scope.me,
        		'receiverUserName': $scope.profileUserName,
        		'message': $scope.comment,
        		'reply': false
        	};
        	$http.post('api/comment/addComment',commentData).
        	success(function(data, status, headers, config){
        		$scope.comment = "";
        		$scope.commentError = null;
        		$scope.getComments();
        		$scope.showComments = true;
        	}).
        	error(function(data, status, headers, config){
        		$scope.commentError = true;
        	});
        };        
        
        Principal.identity().then(function(account) {
            $scope.account = account;
            if($scope.account != null){
            	$scope.me = account.login;
            	$scope.getCommonJourneys();
            }
            else{
            	$scope.me = null;
            }
            $scope.isAuthenticated = Principal.isAuthenticated;
            $scope.isMyProfile = $scope.profileUserName === $scope.me ? true : false;
        });
        
        $scope.getProfile = function(){
        	$http.get('/api/myProfile/findProfile/' + $scope.profileUserName ).
        	success(function(data, status){
        		$scope.profile = data;
        		if($scope.profile.bio == null){
        			$scope.profile.bio = $scope.profileUserName + " has not added a bio yet";
        		}
        	}).
        	error(function(data, status){
        		
        	});
        };
        
        $scope.getCar =  function(){
	    	$http.get('/api/car/getUserCar/' + $scope.profileUserName ).
		    success(function(data, status, headers, config) {
		      $scope.make = data.make;
		      $scope.model = data.model;
		      $scope.year = data.year;
		      if($scope.make == ""){
		      	$scope.typeOfUser = "passenger";
		      	$scope.isDriver = false;
		      	$scope.noCar = $scope.profileUserName + ", Add a car to create journeys";
		      	$scope.carEditAddIcon = "glyphicon glyphicon-plus";
		      	$scope.carEditAdd = "add";
		      }
		      else{
		      	$scope.isDriver = true;
		      	$scope.typeOfUser = "driver/ passenger";
		      	$scope.carEditAddIcon = "glyphicon glyphicon-edit";
		      	$scope.carEditAdd = "edit";
		      }
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });	    	
	    };	    	  
	   
	    $scope.getMyJourneys = function(pageNumber){
	    	$http.get('/api/journey/allJourneys/' + $scope.profileUserName + '?page=' + pageNumber ).
		    success(function(data, status, headers, config) {
			      if(data.first && data.last){
			    		$scope.singlePageJourneys  = true;
			    	}
			      $scope.journeys = data.content;
			      $scope.noJourneys = $scope.journeys <= 0? true:false;
			      $scope.currentPage = data.number + 1;
				  $scope.numPerPage = data.size;
				  $scope.total = data.totalElements;
				  $scope.maxSize = 4; 
				
		    }).error(function(data, status, headers, config) {
		      // log error
		    });
	    };
	    
	    $scope.getMyJourneyRequests = function(pageNumber){
	    	$http.get('/api/journey/allJourneyRequests/' + $scope.profileUserName + '?page=' + pageNumber).
		    success(function(data, status, headers, config) {
		      if(data.first && data.last){
		    		$scope.singlePageJourneyRequests  = true;
		    	}
		      $scope.journeyRequests = data.content;
		      $scope.noJourneyRequests = $scope.journeyRequests <= 0? true:false;
		      $scope.currentPageRequest = data.number + 1;
			  $scope.numPerPageRequest = data.size;
			  $scope.totalRequest = data.totalElements;
			  $scope.maxSizeRequest = 4;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
	    };
	    
	    $scope.getMyBids = function(pageNumber){
	    	$http.get('/api/journeyBid/allBids/' + $scope.profileUserName + '?page=' + pageNumber).
		    success(function(data, status, headers, config) {
		    	if(data.first && data.last){
		    		$scope.singlePageBids  = true;
		    	}
		      $scope.allBids = data.content;
		      $scope.bidData = data;
		      $scope.noBids = $scope.allBids <= 0? true:false;
		      $scope.currentPageRequest = data.number + 1;
			  $scope.numPerPageRequest = data.size;
			  $scope.totalRequest = data.totalElements;
			  $scope.maxSizeRequest = 4;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
	    };
	    
	    $scope.pageChangedRequest = function(page){
        	page--;
        	$scope.getMyJourneyRequests(page);
        	$('div, #userJourneyList').animate({ scrollTop: 0 }, 'medium');
        };
	    
	    $scope.pageChanged = function(page){
        	page--;
        	$scope.getMyJourneys(page);
        	$('div, #userJourneyList').animate({ scrollTop: 0 }, 'medium');
        };
        
        $scope.pageChangedBids = function(page){
        	page--;
        	$scope.getMyBids(page);
        	$('div, #userBidList').animate({ scrollTop: 0 }, 'medium');
        };
        
        $scope.getComments = function(){
        	$http.get('api/comment/getComments/'+ $scope.profileUserName+'?page=0&size=5').
        	success(function(data, status, headers, config){
        		$scope.comments = data.content;
        		if(!data.last){
        			$scope.more = true;
        		}
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.showMoreComments = function(){
        	$scope.currentCommentPage ++;
        	$http.get('api/comment/getComments/'+ $scope.profileUserName +'?page=' + $scope.currentCommentPage+'&size=5').
        	success(function(data, status, headers, config){
        		$scope.comments = $scope.comments.concat(data.content);
        		if(!data.last){
        			$scope.more = true;
        		}
        		else{
        			$scope.more = false;
        		}
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.commentsActive = "active";
        $scope.commentsIsActive = true;
        $scope.reviewsActive = "";
        $scope.reviewsIsActive = false;
        $scope.switchTabs = function(tabClicked){
        	if(tabClicked == 'comments' && !$scope.commentsIsActive || tabClicked == 'reviews' && !$scope.reviewsIsActive){
        		$scope.commentsIsActive = !$scope.commentsIsActive;
        		$scope.reviewsIsActive = !$scope.reviewsIsActive;
        		if($scope.commentsIsActive){
        			$scope.commentsActive = "active";
        			$scope.reviewsActive = "";
        		}
        		else{
        			$scope.commentsActive = "";
        			$scope.reviewsActive = "active";
        		}
        	}
        };
        
        $scope.setPendingNumber = function(journey){
        	if($scope.isMyProfile){
	        	$http.get('api/journeyBid/countBids/'+journey.id+'/pending').
	        	success(function(data, status, headers, config){
	        		journey.numberPending = data.number;
	        	});
        	}
        };
           
        $scope.toggleComments = function(){
        	$scope.getComments();
        	$scope.showComments = true;
        };
        
        $scope.showReviews = true;
        $scope.toggleReviews = function(){
        	$scope.showReviews = true;
        };
        
        $scope.sendReply = function(commentId, comment){
        	var replyMessage = $scope.getReplyMessage(commentId);
        	var commentData = {
        		'senderUserName': $scope.me,
        		'receiverUserName': $scope.profileUserName,
        		'message': replyMessage,
        		'reply': true,
        		'commentId': commentId
        		
        	};
        	$http.post('api/comment/addComment',commentData).
        	success(function(data, status, headers, config){
        		$scope.getReplies(commentId);
        		comment.showme = false;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.getReplyMessage = function(commentId){
        	var message = "";
        	for(var i=0; i<$scope.comments.length; i++){
        		if($scope.comments[i].id == commentId){
        			message = $scope.comments[i].replyMessage;
        			$scope.comments[i].replyMessage = "";
        		}
        	}
        	return message;
        };
        
        $scope.getReplies = function(commentId){
        	$http.get('api/comment/getReplies/' + commentId +'?page=' + 0+'&size=3').
		    success(function(data, status, headers, config) {
		      var replies = data;
		      $scope.addRepliesToComment(commentId, replies);
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };
        
        $scope.addRepliesToComment = function(commentId, replies){
        	for(var i=0; i<$scope.comments.length; i++){
        		if($scope.comments[i].id == commentId){
        			$scope.comments[i].replies = replies;
        		}
        	}        	
        };
        
        $scope.showMorereplies = function(nextPage, commentId){
        	$http.get('api/comment/getReplies/' + commentId +'?page=' + nextPage +'&size=3').
		    success(function(data, status, headers, config) {
		    	$scope.moreReplies(commentId, data);
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };
        
        $scope.moreReplies = function(commentId, data){
        	for(var i=0; i<$scope.comments.length; i++){
        		if($scope.comments[i].id == commentId){
        			$scope.comments[i].replies.content = $scope.comments[i].replies.content.concat(data.content);
        			$scope.comments[i].replies.number = data.number;
        			$scope.comments[i].replies.last = data.last;
        		}
        	}
        };
        
        $scope.sendReview = function(){
        	var reviewData = {
        		'senderUserName': $scope.me,
        		'receiverUserName': $scope.profileUserName,
        		'message': $scope.review,
        		'value': $scope.rate,
        		'journeyId': $scope.journeyId
        		
        	};
        	$http.post('api/review/addReview',reviewData).
        	success(function(data, status, headers, config){
        		$scope.review = "";
        		$scope.rate = null;
        		$scope.getReviews();
        		$scope.toggleReviews();
        		$scope.getAverageReview();
        		$scope.getCommonJourneys();
        		$scope.journeyId = "";
        	}).
        	error(function(data, status, headers, config){
        		if(status == 400){
        			$scope.cantDo = true;
        		}
        	});
        };
        
        $scope.getReviews = function(){
        	$http.get('api/review/getReviews/'+ $scope.profileUserName+'?page=0&size=5').
        	success(function(data, status, headers, config){
        		$scope.reviews = data;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.currentReviewPage = 0;
        $scope.showMoreReview = function(){
        	$scope.currentReviewPage ++;
        	$http.get('api/review/getReviews/'+ $scope.profileUserName +'?page=' + $scope.currentReviewPage+'&size=5').
        	success(function(data, status, headers, config){
        		$scope.reviews.content = $scope.reviews.content.concat(data.content);
        		$scope.reviews.last = data.last;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.getAverageReview = function(){
        	$http.get('api/review/getAverageAndAmount/'+ $scope.profileUserName+'?page=0&size=5').
        	success(function(data, status, headers, config){
        		$scope.reviewData = data;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.getCommonJourneys = function(){
        	$http.get('api/journey/getCommonJourneys/'+ $scope.me+'/' + $scope.profileUserName).
        	success(function(data, status, headers, config){
        		$scope.commonJourneys = data.common;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };    

	    $scope.getUser();
        $scope.getCar();
        $scope.getMyJourneys(0);
        
        $scope.getMyBids(0);
        $scope.getProfile();
        $scope.getAverageReview();
        $scope.getMyJourneyRequests(0);
        $scope.getComments();
        $scope.getReviews();
        $scope.rate;
  		$scope.max = 10;
		$scope.isReadonly = false;

		$scope.hoveringOver = function(value) {
			$scope.overStar = value;
			$scope.percent = 100 * (value / $scope.max);
		};		
		
		$scope.getBidColor = function(status){
			if(status == 'pending'){
				return 'orange';
			}
			else if(status == 'rejected'){
				return 'red';
			}
			else{
				return 'green';
			}	
		};

		$scope.$on('profile', function(event, notification) {
			 if(notification.type == "comment"){
			 	$scope.updateComment(notification.typeId);
			 }
			 else if(notification.type == "reply"){
			 	$scope.updateReply(notification.typeId);
			 }
			 else if(notification.type == "review"){
			 	$scope.updateReview(notification.typeId);
			 }
			 
		});

        $scope.updateComment = function(commentId){
        	$http.get('api/comment/getNewComment/'+commentId).
        	success(function(data, status, headers, config){
        		$scope.newComment = data;
        		if($scope.newComment.receiverUserName == $scope.profileUserName){
        			$scope.comments.unshift($scope.newComment);
        		}
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.updateReply = function(commentId){
        	$http.get('api/comment/getNewComment/'+commentId).
        	success(function(data, status, headers, config){
        		$scope.newReply = data;
        		var comment = $scope.findComment($scope.newReply.commentId);
        		if(comment != null){
        			comment.replies.content.unshift($scope.newReply);
        		}
        		
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.findComment = function(commentToFind){
        	for(var i=0; i< $scope.comments.length; i++){
        	var comment = $scope.comments[i];
        		if(comment.id == commentToFind){
        			return comment;
        		}
        	}
        	return null;
        };
        
        $scope.updateReview = function(reviewId){
        	$http.get('api/review/getNewReview/'+reviewId).
        	success(function(data, status, headers, config){
        		$scope.newReview = data;
        		if($scope.newReview.receiverUserName == $scope.profileUserName){
        			$scope.reviews.content.unshift($scope.newReview);
        		}
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        $scope.tabs = [
           { title:'Dynamic Title 1', content:'#myTab' },
           { title:'Dynamic Title 2', content:'Dynamic content 2'}
       ];
});
