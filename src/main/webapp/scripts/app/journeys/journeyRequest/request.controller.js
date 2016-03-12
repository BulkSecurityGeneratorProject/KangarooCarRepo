'use strict';

angular.module('carshareApp')
    .controller('RequestController', function ($scope,$stateParams, Principal, $translate, $timeout, Auth, $http, UserService) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $scope.id = $stateParams.requestId;
        $http.get('/api/journey/findJourneyRequest/' + $scope.id).
	    success(function(data, status, headers, config) {
	      $scope.journey = data;
	      UserService.journeyId = $scope.journey.id;
	      $scope.showMap = false;
	      $scope.mapButtonText = "Show route";
	      if($scope.account != null){
	      	$scope.showOfferButton = $scope.account.login == $scope.journey.username? false: true;
	      }
	      
	      var directionsService = new google.maps.DirectionsService();
        	var directionsDisplay = new google.maps.DirectionsRenderer({ 'map': map, 'draggable': false});
        	var request = {
			    origin: $scope.journey.source,
			    destination: $scope.journey.destination,
			    travelMode: google.maps.TravelMode.DRIVING
		  	};
		  directionsService.route(request, function(result, status) {
		    if (status == google.maps.DirectionsStatus.OK) {
		      directionsDisplay.setDirections(result);
		      var leg = result.routes[ 0 ].legs[ 0 ];
			  
			  var sourceMarker = new google.maps.Marker({
			     position: leg.start_location,
			     animation: google.maps.Animation.DROP,
			     map: map
			 });
			 var destinationMarker = new google.maps.Marker({
			     position: leg.end_location,
			     animation: google.maps.Animation.DROP,
			     map: map
			 });
		    }
		  });
		  
		  
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    });
	    

	    $scope.goBack = function(){
	    	window.history.back();
	    };
	    
    	$scope.book = function(){
	    	alert("book the journey");
	    };
	    
	    $scope.showOnMap = function(){
	    	if($scope.showMap == false){
	    		$scope.showMap = true;
	    		$scope.mapButtonText = "Hide";
	    	}
	    	else{
	    		$scope.showMap = false;
	    		$scope.mapButtonText = "Show route";
	    	}
	    };
	    $scope.showMap = true;
	    
    });
