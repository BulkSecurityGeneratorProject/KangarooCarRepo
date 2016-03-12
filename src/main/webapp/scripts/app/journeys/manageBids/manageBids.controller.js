'use strict';

angular.module('carshareApp')
    .controller('ManageBidsController', function ($scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService, $state) {
        Principal.identity().then(function(account) {
            if(account == null){
            	$state.go('login');
            }
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $http.get('/api/journey/findJourney/' + $stateParams.journeyId).
        success(function(data, status, headers, config){
        	$scope.journey = data;
        	if($scope.journey.username != $scope.account.login){
        		$state.go('accessdenied');
        	}
        	$scope.getBids();
        }).
        error(function(data, status, headers, config){
        	$state.go('error');
        });
        $scope.values = ['All','pending','accepted','rejected'];
        
        $scope.getBids = function(){
        	$http.get('/api/journeyBid/allBidsForJourney/'+ $scope.journey.id).
        	success(function(data, status, headers, config){
        		$scope.bids = data.enquires;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        
        $scope.acceptBid = function(idForBid){
        	$http.get('/api/journeyBid/bidForJourney/'+idForBid+'/accepted').
        	success(function(data, status, headers, config){
        		$state.reload();
        	}).
        	error(function(){
        		$state.go('error');
        	});
        };
        
        $scope.rejectBid = function(idForBid){
        	$http.get('/api/journeyBid/bidForJourney/'+idForBid+'/rejected').
        	success(function(data, status, headers, config){
        		$state.reload();
        	}).
        	error(function(){
        		$state.go('error');
        	});
        };
        
        $scope.goBack = function(){
	    	window.history.back();
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
		
		$(window).resize(function(){
		    $scope.windowWidth = window.innerWidth;
		
		    $scope.$apply(function(){
		       if($scope.windowWidth <1000){
		       		$scope.colSize = "col-sm-6";
		       }
		       else{
		       		$scope.colSize = "col-sm-4";
		       }
		    });
		});
		$scope.windowWidth = window.innerWidth;
		if($scope.windowWidth <1000){
       		$scope.colSize = "col-sm-6";
       	}
       	else{
       		$scope.colSize = "col-sm-4";
       	}
    });
