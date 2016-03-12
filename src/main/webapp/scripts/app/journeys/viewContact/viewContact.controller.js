'use strict';

angular.module('carshareApp')
// <<<<<<< HEAD
    .controller('ViewContactController', function ($scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService, $state) {
        var vm = this;
  
  		vm.time = new Date();
        
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
        $http.get('/api/journey/findJourneyRequest/' + $stateParams.journeyRequestId).
        success(function(data, status, headers, config){
        	$scope.journey = data;
        	if($scope.journey.username != $scope.account.login){
        		$state.go('accessdenied');
        	}
        	$scope.getContact();
        }).
        error(function(data, status, headers, config){
        	$state.go('error');
        });
        
        $scope.getContact = function(){
        	$http.get('/api/contact/allContactsForJourney/'+ $scope.journey.id).
        	success(function(data, status, headers, config){
        		$scope.contacts = data.enquires;
        	}).
        	error(function(data, status, headers, config){
        		
        	});
        };
        $scope.goBack = function(){
	    	window.history.back();
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
// =======
// 	.controller('ViewContactController', function ($scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService, $state) {
// 		var vm = this;	
// 		vm.time = new Date();
	
// 		Principal.identity().then(function(account) {
// 			$scope.account = account;
// 			$scope.isAuthenticated = Principal.isAuthenticated;
// 		});
	
// 		$http.get('/api/journey/findJourneyRequest/' + $stateParams.journeyRequestId).
// 		success(function(data, status, headers, config){
// 			$scope.journey = data;
// 			if($scope.journey.username != $scope.account.login){
// 				$state.go('accessdenied');
// 			}
// 			$scope.getContact();
// 		}).
// 		error(function(data, status, headers, config){
// 			$state.go('error');
// 		});
	
// 		$scope.getContact = function(){
// 			$http.get('/api/contact/allContactsForJourney/'+ $scope.journey.id).
// 			success(function(data, status, headers, config){
// 				$scope.contacts = data.enquires;
// 			}).
// 			error(function(data, status, headers, config){
	
// 			});
// 		};
// 		$scope.goBack = function(){
// 			window.history.back();
// 		};
// });
// >>>>>>> 9477a58f16b016696e12b29bb4aeb11316f0fd1a
