'use strict';

angular.module('carshareApp')
    .controller('MainController', function ($scope, Principal, $state, $http, $timeout) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;                       
        });
        
        $http.get('/api/journey/allJourneys?page=0&size=5').//http://fsgw71589a:9000
		    success(function(data, status, headers, config) {
		      $scope.journeys = data.content;
		      
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });

        $scope.myInterval = 4000;
		  
		  
		$scope.goToResults = function(){
			$state.go("findJourneys",{from: $scope.from, to: $scope.to})
		}
		/*var stompNotificationClient = null;
		
		$scope.connect = function(){
	        var notificationSocket = new SockJS('/notifications/test1');
	        stompNotificationClient = Stomp.over(notificationSocket);
	
	        stompNotificationClient.connect({}, function(frame) {
	            console.log('Connected: ' + frame);
	            stompNotificationClient.subscribe('/user/'+$scope.account.login+'/get/notifications/', function(greeting){
	                $scope.showGreeting(JSON.parse(greeting.body).content);
	            });
	        });
	    }
    
    $scope.disconnect = function() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }

    $scope.sendNotification = function(){
        var name = "test"
        stompNotificationClient.send("/gssocket/notifications/test1", {}, JSON.stringify({ 'name': name }));
    }
	
	$scope.showGreeting = function(greeting){
		alert(greeting);
	}*/

    });
