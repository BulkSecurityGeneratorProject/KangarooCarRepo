'use strict';

angular.module('carshareApp')
    .controller('AddJourneyRequestController', function ($scope, $log, $filter, $http, Principal, $document, $state) {
    	$scope.success = null;
    	$scope.error = null;
    	var directionsService = null;
	    var directionsDisplay = new google.maps.DirectionsRenderer();
	    $scope.journeyRequest.source = "";
		$scope.journeyRequest.destination = "";
		$scope.$watch('[journeyRequest.source, journeyRequest.destination]', function () { $scope.placeChange(); }, true);
		
    	var username;
    	 Principal.identity().then(function(account) {
             $scope.account = account;
             username = account.login;
             $scope.isAuthenticated = Principal.isAuthenticated;
         });
    	
		//Date  
    	var clickedDate = false;
    	var clickedTime = false;
   
		$scope.hstep = 1;
		$scope.mstep = 15;
		$scope.toggleMin = function() {			
			$scope.minDate = $scope.minDate ? null : new Date();
		};
		$scope.toggleToday = function() {
			$scope.labelDate = new Date();		
		};
		$scope.toggleTime = function() {
			$scope.labelTime = new Date();		
		};
		$scope.ismeridian = false;
		$scope.toggleMode = function() {
		    $scope.ismeridian = ! $scope.ismeridian;
		};
		
		//Form
		//Add user name
		$scope.addUsername = function() {
			$scope.journeyRequest.username = username;
		};
		//Add date
		$scope.addDate = function() {
			$scope.journeyRequest.date =  angular.element('#dateTxt').val();
		};
		//Add time
		$scope.addTime = function() {
			$scope.journeyRequest.time =  angular.element('#timeTxt').val();
		};
		
		$scope.resetTextBoxes = function() {
			$scope.journeyRequest.source = "";
			$scope.journeyRequest.destination = "";
			$scope.journeyRequest.pickUpLocation = "";
		};
		
		$scope.display = function() {
			$log.log(JSON.stringify($scope.journeyRequest));
		};
		$scope.showJourneyRequest = function(){
        	$http.get('/api/journey/allJourneyRequests/'+ username +'?page=0&size=1').
		    success(function(data, status, headers, config) {		    	
		      $scope.newJourneyRequest = data.content[0];
		      $state.go('request', {requestId: $scope.newJourneyRequest.id});
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };
		  
		$scope.processForm = function() {			
			$http.post("/api/journey/registerJourneyRequest", $scope.journeyRequest).
			success(function(data, status, headers, config){
				$scope.success = 'OK';
				$scope.resetTextBoxes();
				$scope.showJourneyRequest();
			})
        	.error(function(response) { 
        		$scope.success = null;
                if (response.status === 500) {
                	$scope.error = 'ERROR';
                } 
    		});
		};     
		$scope.resetError = function() {
			$scope.error = null;
		};   
		$('[data-toggle="tooltip"]').tooltip();
		
		var sourceMarker = new google.maps.Marker();
		var destinationMarker = new google.maps.Marker();
		
		$scope.placeChange = function(){
			directionsDisplay.setMap(null);
			sourceMarker.setMap(null);
			destinationMarker.setMap(null);
			directionsService = new google.maps.DirectionsService();
	    	directionsDisplay = new google.maps.DirectionsRenderer({ 'map': map, 'draggable': false});
	    	var request = {
			    origin: $scope.journeyRequest.source,
			    destination: $scope.journeyRequest.destination,
			    travelMode: google.maps.TravelMode.DRIVING
		  	};
		  	
		  	directionsService.route(request, function(result, status) {
		    if (status == google.maps.DirectionsStatus.OK) {
		      directionsDisplay.setDirections(result);
		      var leg = result.routes[ 0 ].legs[ 0 ];
			  
			  sourceMarker = new google.maps.Marker({
			     position: leg.start_location,
			     animation: google.maps.Animation.DROP,
			     map: map
			 });
			 destinationMarker = new google.maps.Marker({
			     position: leg.end_location,
			     animation: google.maps.Animation.DROP,
			     map: map
			 });
		    }
		  });
		  	
		}
		
		
		
});
