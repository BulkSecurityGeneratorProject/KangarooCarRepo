'use strict';

angular.module('carshareApp')
    .controller('JourneyController', function ($rootScope, $scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService, $state, $log) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $scope.notOnJourney = false;
        $scope.amPassenger = false;
        $scope.windowHeight = $("#page").innerHeight() -20;
        $scope.windowWidth = $("body").innerWidth();
        $scope.waypointsPlaceHolder="";
        $scope.id = $stateParams.journeyId;
        $scope.showComments = true;
        $scope.journeyId = $stateParams.journeyId;
        $scope.more = false;
        $scope.currentCommentPage = 0;
        $scope.datePassed = false;
        $scope.label = "";
        $scope.showButton =true;
      	var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!
		var yyyy = today.getFullYear();
		
		if(dd<10) {
		    dd='0'+dd;
		} 
		
		if(mm<10) {
		    mm='0'+mm;
		} 
		
		today = mm+'/'+dd+'/'+yyyy;
		$scope.today = new Date(today);

		$scope.getLetter = function(letter){
			return String.fromCharCode(66+letter);
		};

		$scope.waypointName = function(arr){
			var geocoder;
			$scope.waypointNamesArr = [];
			geocoder = new google.maps.Geocoder();

			for(var i = 0; i< arr.length; i++){
				var latlng = new google.maps.LatLng(arr[i].lat, arr[i].lng);
				geocoder.geocode({
					'latLng': latlng
				}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						if (results[0]) {
							var add = results[0].formatted_address;
							$scope.waypointNamesArr.push(""+add);
						} else {
							$scope.waypointNamesArr.push("unnamed location");
						}
					} else {
						$scope.waypointNamesArr.push("unnamed location");
					}
				});		
				var myMarker = new google.maps.Marker({
				     position: new google.maps.LatLng(arr[i].lat, arr[i].lng),
				     animation: google.maps.Animation.DROP,
				     map: map
				 });

			}

			$scope.hasBeenReviewed = true;
			if($scope.account != null){
				$scope.driverAlreadyReviewed($scope.account.login, $scope.journey.username, $scope.journey.id);
			}
			
		};
        
        $http.get('/api/journey/findJourney/' + $scope.id).
	    success(function(data, status, headers, config) {
		    $scope.journey = data;
		    $rootScope.suggestedCost = data.cost;
		    $scope.date = new Date($scope.journey.date);
		    if($scope.today.getTime() > $scope.date.getTime()){
		      	$scope.datePassed = true;
		    }
	      
		    var sourceMarker = new google.maps.Marker({
			    position: new google.maps.LatLng($scope.journey.sourceLat, $scope.journey.sourceLng),
			    animation: google.maps.Animation.DROP,
			    map: map
			 });
		 
			 var destinationMarker = new google.maps.Marker({
			     position: new google.maps.LatLng($scope.journey.destinationLat, $scope.journey.destinationLng),
			     animation: google.maps.Animation.DROP,
			     map: map
			 });
	      
			$scope.srcLatLng = [$scope.journey.sourceLat, $scope.journey.sourceLng];
			$scope.destLatLng = [$scope.journey.destinationLat, $scope.journey.destinationLng];

			$scope.choseLabel();
			$scope.getPassengers();
			if($scope.account == null){
				$scope.notMyJourney = true;
			}
			else{
				$scope.notMyJourney = $scope.journey.username != $scope.account.login ? true: false;
			}
			$scope.waypointName($scope.journey.waypts);
			UserService.journeyId = $scope.journey.id;
			$scope.waypointsFromServer = data.waypts;
			$scope.source = data.source;
			$scope.destination = data.destination;
			$scope.buildWaypoints();
			$scope.getCar(data.username);
			
			$scope.mapButtonText = "Show route";
	      
		    var myDirectionsService = new google.maps.DirectionsService();
	        var myDirectionsDisplay = new google.maps.DirectionsRenderer({ 'map': map, 'draggable': false});
	        var request = {
			    origin: new google.maps.LatLng($scope.journey.sourceLat, $scope.journey.sourceLng),
			    waypoints: $scope.waypointsArr,
			    destination: new google.maps.LatLng($scope.journey.destinationLat, $scope.journey.destinationLng),
			    travelMode: google.maps.TravelMode.DRIVING
			};
		  	myDirectionsService.route(request, function(result, status) {
		    	if (status == google.maps.DirectionsStatus.OK) {
		      		myDirectionsDisplay.setDirections(result);
		      		$scope.route = myDirectionsDisplay.directions;
		      		$scope.computeTotalDistance();
		      		$('#hiddenButton').click();
		    	}
		  	});
	    }).
	    error(function(data, status, headers, config) {
	    	$state.go('error');
	    }); 
	    
	    
	    $scope.choseLabel = function(){
	    	if($scope.datePassed){
	    		$scope.label = "Closed";
	    	}
	    	else if($scope.journey.seatNumber <=0){
	    		$scope.label = "Full";
	    	}
	    };	    
	    $scope.getCar = function(uname){
	    	$http.get('/api/car/getUserCar/' + uname ).
		    success(function(data, status, headers, config) {
		      $scope.car = data.make +" "+ data.model;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
	    }; 	    
	    $scope.buildWaypoints = function(){
	    	if($scope.waypointsFromServer.length != 0){
	    		$scope.waypointsArr = [];
	    		for(var i=0; i < $scope.waypointsFromServer.length; i++){
	    			$scope.waypointsArr.push({ location: $scope.waypointsFromServer[i].lat+","+ $scope.waypointsFromServer[i].lng});
	    			$log.log($scope.waypointsFromServer[i].lat+","+ $scope.waypointsFromServer[i].lng);
	    		}
	    		$scope.hasWawpoints = true;
	    		$scope.waypointsPlaceHolder =  $scope.waypointsArr;    		
	    	}
	    	else{
	    		$scope.hasWawpoints = false;
	    		$scope.waypointsPlaceHolder = "";
	    	}
	    };	    
	    $scope.madeReview = function(me, them, passenger){
        	
	    	if(me != them && $scope.isAuthenticated){
        		$http.get('api/review/madeReview/'+ me+'/' + them+'/'+$scope.journey.id).
	        	success(function(data, status, headers, config){
	        		var madeReview = data.hasReview;
	        		passenger.hasBeenReviewed = madeReview;
	        		
	        	}).
	        	error(function(data, status, headers, config){
	        		
	        	});
        	}
        	else{
        		passenger.hasBeenReviewed = true;
        	}
        	
        };	    
	    $scope.getPassengers = function(){
	    	$http.get('/api/journey/getPassengers/' + $scope.journey.id + '/' + $scope.journey.username ).
		    success(function(data, status, headers, config) {
		      $scope.passengers = data.passengers;
		      var count =0;
		      for(var i = 0; i < $scope.passengers.length; i++) {
				    if(!$scope.isAuthenticated() || !$scope.datePassed){
				    	$scope.passengers[i].hasBeenReviewed = true;
				    }
				    else{
				    	$scope.madeReview($scope.account.login, $scope.passengers[i].userName, $scope.passengers[i]);
				    }
				    if($scope.account != null){
				    	if($scope.passengers[i].userName == $scope.account.login){
					    	$scope.amPassenger = true;
					    }
				    	if($scope.account.login == $scope.passengers[i].userName || $scope.journey.username == $scope.account.login){
				    		count ++;
				    	}
				    }		    	  
			  }
		      
		      if(count<=0){
		    	  $scope.notOnJourney = true;
		      }
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
	    };
	    
	    $scope.goBack = function(){
	    	window.history.back();
	    };
	    
    	$scope.book = function(){
	    	alert("book the journey");
	    };
	    
	    $scope.getNumber = function(num) {
		    return new Array(num);   
		};
	    
	    $scope.showOnMap = function(){
	    	if($scope.showMap == false){
	    		$scope.showButton = false;
	    		$scope.showMap = true;
	    	}
	    	else{
	    		$scope.showMap = false;
	    	}
	    };	    
	    $scope.toggleComments = function(){
        	$scope.getComments();
        	$scope.showComments = true;
        };        
        $scope.getComments = function(){
        	$http.get('api/comment/getJourneyComments/'+ $scope.journeyId+'?page=0&size=5').
        	success(function(data, status, headers, config){
        		$scope.comments = data.content;
        		if(!data.last){
        			$scope.more = true;
        		}
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        $scope.getComments();
        
        $scope.showMoreComments = function(){
        	$scope.currentCommentPage ++;
        	$http.get('api/comment/getJourneyComments/'+ $scope.journeyId +'?page=' + $scope.currentCommentPage+'&size=5').
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
        		alert("test");
        	});
        };        
        $scope.postComment = function(){
        	var commentData = {
        		'senderUserName': $scope.account.login,
        		'journeyId': $scope.journeyId,
        		'message': $scope.comment,
        		'reply': false
        	};
        	$http.post('api/comment/addJourneyComment',commentData).
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
        $scope.sendReply = function(commentId, comment){
        	var replyMessage = $scope.getReplyMessage(commentId);
        	var commentData = {
        		'senderUserName': $scope.account.login,
        		'journeyId': $scope.journeyId,
        		'message': replyMessage,
        		'reply': true,
        		'commentId': commentId
        		
        	};
        	$http.post('api/comment/addJourneyComment',commentData).
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
        	$http.get('api/comment/getJourneyReplies/' + commentId +'?page=' + 0+'&size=3').
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
        $scope.moreReplies = function(commentId, data){
        	for(var i=0; i<$scope.comments.length; i++){
        		if($scope.comments[i].id == commentId){
        			$scope.comments[i].replies.content = $scope.comments[i].replies.content.concat(data.content);
        			$scope.comments[i].replies.number = data.number;
        			$scope.comments[i].replies.last = data.last;
        		}
        	}
        };        
        $scope.showMorereplies = function(nextPage, commentId){
        	$http.get('api/comment/getJourneyReplies/' + commentId +'?page=' + nextPage +'&size=3').
		    success(function(data, status, headers, config) {
		    	$scope.moreReplies(commentId, data);
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };
        
        $scope.toggleFullScreen = function(){
        	$scope.fullScreen = !$scope.fullScreen;
        };   
        
        $scope.computeTotalDistance = function() {
		  var total = 0;
		  var myroute = $scope.route.routes[0];
		  for (var i = 0; i < myroute.legs.length; i++) {
		    total += myroute.legs[i].distance.value;
		  }
		  total = total / 1000.0;
		  
		  var myStuff=0;
		  for(var i = 0;i < myroute.legs.length; i++){
		    myStuff += myroute.legs[i].duration.value;
		  }
		  myStuff=myStuff/60;
		  myStuff=myStuff.toFixed(2);
		  total = total.toFixed(2);
		  
		  $scope.calculatedDistance = total;
		  $scope.time = myStuff | 0;
		  $scope.duration = $scope.getDuration($scope.time);
		  $scope.arrivalTime = $scope.getArrivalTime($scope.journey.time,$scope.time);
		};
			
		$scope.getTime= function(mins){
			mins = Math.round(mins);
			var hours = Math.floor( mins / 60);          
    		var minutes = mins % 60;   		

		}
		
		$scope.getDuration= function(mins){
			mins = Math.round(mins);
			var hours = Math.floor( mins / 60);          
    		var minutes = mins % 60;
    		return hours + " hours " + minutes +" mins";
		};		
		$scope.getArrivalTime = function(time, minsToAdd){
			time = time.substring(0,time.length-3);
			function z(n){ return (n<10? '0':'') + n;};
			var bits = time.split(':');
			var mins = bits[0]*60 + +bits[1] + +minsToAdd;
			return z(mins%(24*60)/60 | 0) + ':' + z(mins%60); 
		};		
		$scope.driverAlreadyReviewed = function(me, driver, journeyId){
			$http.get('api/review/madeReview/'+ me+'/' + driver+'/'+$scope.journey.id).
	        	success(function(data, status, headers, config){
	        		var madeReview = data.hasReview;
	        		$scope.hasBeenReviewed = madeReview;
	        		
	        	}).
	        	error(function(data, status, headers, config){
	        		
	        	});
		};			
		$scope.canReviewDriver = function(){
			if($scope.isAuthenticated() || $scope.account != null){
				if(!$scope.hasBeenReviewed){
					return true;
				}
			}
			return false;
		};		
		$scope.arePassenger = function(){
			for(var i = 0; i < $scope.passengers.length; i++) {
			    if($scope.passengers[i].userName == $scope.account.login){
			    	return true;
			    }
			}
			return false;
		};					
		$scope.showReviewButton = function(userName){
			if($scope.account != null)
			{
				if($scope.account.login != userName){
					return true;
				}
			}
			return false;
		};

    });

