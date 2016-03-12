'use strict';

angular.module('carshareApp')
    .controller('AddJourneyController', function ($scope, $log, $filter, $http, Principal, $document, $state) {    	
    	$scope.success = null;
    	$scope.error = null;
    	$scope.waypts = [];
    	$scope.calculatedDistance = 0;
    	$scope.time = 0;
    	$scope.recomendedCost = 0;
    	var username;
    	 Principal.identity().then(function(account) {
             $scope.account = account;
             username = account.login;
             $scope.isAuthenticated = Principal.isAuthenticated;
         });
    	 $scope.removeRoute = function() {
    		 $scope.journey.waypts = [];
    		 $state.reload();
    	 };   	 
    	 //Map
    	 $scope.waypts=[];    	 
		 var sourceMarker = new google.maps.Marker();
		 var destinationMarker = new google.maps.Marker();
    	 var w=[];
    	 var data = {};
    	 var waypointsArr = [];
    	
    	 $scope.calcRoute = function() {
    	 		
			var MyDirectionsDisplay = new google.maps.DirectionsRenderer({ 'map': map, 'draggable': true});		
			
			var start = angular.element('#source').val();     	
			var end = angular.element('#destination').val();
			google.maps.event.addListener(MyDirectionsDisplay, 'directions_changed', function(e) {				
				sourceMarker.setMap(null);
				destinationMarker.setMap(null);
				$scope.waypts = [];
				var routeLeg = MyDirectionsDisplay.directions.routes[0].legs[0];
				var legSize = MyDirectionsDisplay.directions.routes[0].legs.length;
				var routeLegEnd = MyDirectionsDisplay.directions.routes[0].legs[legSize - 1];

				data.start = {'lat': routeLeg.start_location.lat(), 'lng':routeLeg.start_location.lng()};
				data.end = {'lat': routeLegEnd.end_location.lat(), 'lng':routeLegEnd.end_location.lng()};			   
				var wp = routeLeg.via_waypoints;

				//create latLng for journey
				$scope.journey.sourceLat = routeLeg.start_location.lat();
				$scope.journey.sourceLng = routeLeg.start_location.lng();
				sourceMarker = new google.maps.Marker({
					     position: new google.maps.LatLng($scope.journey.sourceLat, $scope.journey.sourceLng),
					     animation: google.maps.Animation.DROP,
					     map: map
					 });
				$scope.journey.destinationLat = routeLegEnd.end_location.lat();
				$scope.journey.destinationLng = routeLegEnd.end_location.lng();
				destinationMarker = new google.maps.Marker({
					     position: new google.maps.LatLng($scope.journey.destinationLat, $scope.journey.destinationLng),
					     animation: google.maps.Animation.DROP,
					     map: map
					 }); 

				for(var i=0;i<wp.length;i++) {
					w[i] = [wp[i].lat(),wp[i].lng()];
					var waypointObj = {'lat':wp[i].lat(), 'lng': wp[i].lng()};
					waypointsArr.push(waypointObj);
					codeLatLng(wp[i].lat(),wp[i].lng());
					var myMarker = new google.maps.Marker({
					     position: new google.maps.LatLng(wp[i].lat(), wp[i].lng()),
					     animation: google.maps.Animation.DROP,
					     map: map
					 });		    	 
				}
				$scope.journey.waypts = waypointsArr;

				data.waypoints = w;			    

				var service = new google.maps.DistanceMatrixService();
				service.getDistanceMatrix(
						{
							origins:[MyDirectionsDisplay.directions.routes[0].legs[0].start_location],
							destinations:[MyDirectionsDisplay.directions.routes[0].legs[MyDirectionsDisplay.directions.routes[0].legs.length-1].end_location],
							travelMode: google.maps.TravelMode.DRIVING
						},callback);
				$scope.computeTotalDistance(MyDirectionsDisplay.directions);
			});	

			var request = {
					origin:start,
					waypoints: $scope.journey.waypts,
					destination:end,
					travelMode: google.maps.TravelMode.DRIVING
			};	

			var MyDirectionsService = new google.maps.DirectionsService(); 	
			MyDirectionsService.route( request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) {
					MyDirectionsDisplay.setDirections(response);									
					var distance = 0;
					for(var i = 0; i < response.routes[0].legs.length; i++){
						distance += response.routes[0].legs[i].distance.value / 1000;
					}
					var dist = Math.round(distance * 100) / 100 + " KM";					
					//document.getElementById('distanceLabel').innerHTML = dist;			
				}
			});	
		};
		function callback(response, status) {
			if (status == google.maps.DistanceMatrixStatus.OK) {
				var origins = response.originAddresses;
			    var destinations = response.destinationAddresses;
			    var outputDiv = document.getElementById('distanceLabel');
			   // outputDiv.innerHTML = '';				   
			    for (var i = 0; i < origins.length; i++) {
			        var results = response.rows[i].elements;			       
			        for (var j = 0; j < results.length; j++) {			        
			          //outputDiv.innerHTML += results[j].distance.text + ' in '+ results[j].duration.text + '<br>';
			        }
			    }
			} 
		}	
		var arrOfWatpointsArr = [];
		var strWaypoint;
		function codeLatLng(lat, lng) {	
			var geocoder = new google.maps.Geocoder();
			var latlng = new google.maps.LatLng(lat, lng);
			geocoder.geocode({'latLng': latlng}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					strWaypoint = results[1].formatted_address;			    	
					$scope.waypts.push(strWaypoint);
					$log.log($scope.waypts);					
				}
				arrOfWatpointsArr.push($scope.waypts);
			});			
		};		 
	   
		//Date  
    	var clickedDate = false;
    	var clickedTime = false;    	
    	
		$scope.hstep = 1;
		$scope.mstep = 15;
	
		$scope.ismeridian = false;
		$scope.toggleMode = function() {
		    $scope.ismeridian = ! $scope.ismeridian;
		};
		$scope.toggleMin = function() {			
			$scope.minDate = $scope.minDate ? null : new Date();
		};
		$scope.toggleToday = function() {
			$scope.labelDate = new Date();		
		};
		$scope.toggleTime = function() {
			$scope.labelTime = new Date();		
		};			    
	    // Select boxes
	    $scope.seatsArr  = [1, 2, 3, 4];
	    $scope.luggageArr  = ['None','Small','Medium','Large'];
	    $scope.detourArr  = ['0km','1km','2km','5km','10km','20km']; 	
		//Form
		//Add user name
		$scope.addUsername = function() {
			$scope.journey.username = username;
		};
		//Add date
		$scope.addDate = function() {
			var myDate =  angular.element('#dateTxt').val();
			$scope.journey.date = Date.parse($scope.labelDate);
		};
		//Add time		
		$scope.addTime = function() {	
			$scope.journey.time =  angular.element('#timeTxt').val();
		};
		$scope.labelDate = angular.element('#dateTxt').val();
		$scope.labelTime = angular.element('#timeTxt').val();
		
		//Display journey
		$scope.display = function() {
			$log.log(JSON.stringify($scope.journey));
		};	
		
		$scope.resetTextBoxes = function() {
			$scope.journey.source = "";
			$scope.journey.destination = "";
		};
		$scope.showJourney = function(){
        	$http.get('/api/journey/allJourneys/'+ username +'?page=0&size=1').
		    success(function(data, status, headers, config) {		    	
		      $scope.newJourney = data.content[0];
		      $state.go('journey', {journeyId: $scope.newJourney.id});
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };     
				
        $scope.processForm = function() {
			$http.post("/api/journey/registerJourney", $scope.journey)
			.success(function(data, status, headers, config){
				$scope.success = 'OK';
				$scope.resetTextBoxes();
				$scope.showJourney();
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
		$scope.computeTotalDistance = function(result) {
			var total = 0;
			var myroute = result.routes[0];
			for (var i = 0; i < myroute.legs.length; i++) {
				total += myroute.legs[i].distance.value;
			}
			total = total / 1000.0;

			var thresholds=[0,0,64,100,150];   
			var ranges=[0,0.225,0.2,0.15,0.1];  

			$scope.totalDist= total;
			if(ranges.length===thresholds.length){
				var cost = 0;
				var abort = true;
				for(var i=1;i<thresholds.length;i++){

					if(total>thresholds[i]&&i!==thresholds.length-1){
						cost+=(thresholds[i]-thresholds[i-1])*ranges[i-1];
					}
					if(total<thresholds[i]&&abort){
						cost+=(total-thresholds[i-1])*ranges[i-1];
						abort=false;
					}

					if(i===thresholds.length-1&&total>thresholds[i]){
						cost+=(total-thresholds[i])*ranges[i];
					}
				}
				abort=true;
			}
			var fuelCost = ($scope.litres/100)*$scope.fuelPrice*total;

			var myStuff=0;
			for(var i = 0;i < myroute.legs.length; i++){
				myStuff += myroute.legs[i].duration.value;
			}

			myStuff=myStuff/60;
			$scope.totalTime=myStuff;
			myStuff=myStuff.toFixed(2);
			cost = cost.toFixed(2);
			total = total.toFixed(2);
			computeSmallDistance(result);

			document.getElementById('distanceAndTime').innerHTML = total + ' km in ' + $scope.getTime(myStuff);
			$scope.calculatedDistance = total;
			$scope.time = myStuff;
			$scope.recomendedCost = parseFloat(cost);
			$scope.journey.cost = parseFloat(cost);
		
			google.maps.event.trigger(map, 'resize');
		};	

		$('[data-toggle="tooltip"]').tooltip();

		function computeSmallDistance(result){
			var total = 0;
			var service = new google.maps.DistanceMatrixService();
			service.getDistanceMatrix(
					{
						origins:[result.routes[0].legs[0].start_location],
						destinations:[result.routes[0].legs[result.routes[0].legs.length-1].end_location],
						travelMode: google.maps.TravelMode.DRIVING
					},callback);
			function callback(response, status){
				if (status == google.maps.DistanceMatrixStatus.OK) {
					var total = response.rows[0].elements[0].distance.value/1000;
				}
				$scope.totalDetour= total;
				var thresholds=[0,0,64,100,150];  
				var ranges=[0,0.225,0.2,0.15,0.1]; 
				var threshold=[0,64,100,150];   
				var range=[0.225,0.2,0.15,0.1]; 
				if(ranges.length===thresholds.length){
					var cost = 0;
					var abort = true;
					for(var i=1;i<thresholds.length;i++){
						if(total>thresholds[i]&&i!==thresholds.length-1){
							cost+=(thresholds[i]-thresholds[i-1])*ranges[i-1];
						}
						if(total<thresholds[i]&&abort){
							cost+=(total-thresholds[i-1])*ranges[i-1];
							abort=false;
						}

						if(i===thresholds.length-1&&total>thresholds[i]){
							cost+=(total-thresholds[i])*ranges[i];
						}
					}
					abort=true;
				}
				var myStuff = response.rows[0].elements[0].duration.value;
			}
		}		
		$scope.getTime= function(mins){
			mins = Math.round(mins);
			var hours = Math.floor( mins / 60);          
			var minutes = mins % 60;
			return hours + " hours " + minutes +" mins";
		};
    });

