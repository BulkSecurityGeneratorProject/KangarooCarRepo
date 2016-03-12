'use strict';

angular.module('carshareApp')
    .controller('NotificationsController', function ($scope, Principal, $state, $http, $timeout) {
        $scope.pageNumber = 0;
        $scope.getNotifications = function(number){
        	$http.get('/api/notifications/allNotifications/'+$scope.account.login+'?page='+number+'&size=10').
		    success(function(data, status, headers, config) {
		      $scope.notifications = data;
		      $scope.hasMore = $scope.notifications.last == false ? true: false;
		      $scope.pageNumber++;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        }
        
        Principal.identity().then(function(account) {
            if(account == null){
            	$state.go('home');
            }
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            $scope.getNotifications($scope.pageNumber);                  
        });
        
        $scope.showMore = function(){
        	$http.get('/api/notifications/allNotifications/'+$scope.account.login+'?page='+$scope.pageNumber+'&size=10').
		    success(function(data, status, headers, config) {
		      $scope.moreNotifications = data;
		      $scope.hasMore = $scope.moreNotifications.last == false ? true: false;
		      $scope.notifications.content = $scope.notifications.content.concat($scope.moreNotifications.content);
		      $scope.pageNumber++;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        }   
        
        $scope.linkTo = function(link){
        	var where = link[0];
        	var param = link[1];
        	var paramValue = link[2];
        	if(param == 'username'){
        		$state.go(where,{username: paramValue});
        	}
        	else if(param == 'journeyId'){
        		$state.go(where,{journeyId: paramValue});
        	}
        	else if(param == 'journeyRequestId'){
        		$state.go(where,{journeyRequestId: paramValue});
        	}
        	
        	
        }
    });
