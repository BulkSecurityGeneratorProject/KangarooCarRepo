'use strict';

angular.module('carshareApp')
    .controller('NavbarController', function ($rootScope,$scope, $location, $state, Auth, Principal, $log, $http, Notification) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.notificationsCount = 0;
        var sound = document.getElementById("sound");
        var stompNotificationClient;
        var notificationSocket = null;
        $scope.newNotifications = [];
        $scope.connect = function(me){
	        notificationSocket = new SockJS('/notifications/test1');
	        stompNotificationClient = Stomp.over(notificationSocket);
	
	        stompNotificationClient.connect({}, function(frame) {
	            console.log('Connected: ' + frame);
	            stompNotificationClient.subscribe('/user/'+me+'/get/notifications/', function(greeting){
	                var notification = JSON.parse(greeting.body); 
	                Notification.success({message: "<div><a href ui-sref='profile({username: "+notification.senderName+"})''><img src='/user/profile/getProfilePicture/"+ notification.senderName +"' class='notificationPic' > "+ notification.senderName +"</a> "+ notification.message+"</div>", delay: 7000});
	                sound.play();
	            	$scope.notificationsCount ++;
	            	$scope.newNotifications.unshift(notification);
	            	$rootScope.$broadcast(notification.link[0], notification);
	            });
	        });
	        
	        notificationSocket.onclose = function (event) {            
			    $scope.retryOpeningWebSocket();
			};
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
        	
        	
        };
        
		 
		$scope.retryOpeningWebSocket = function(){
		    var retries = 0;
		    if (retries < 2) {            
		        setTimeout(openWebSocket, 1000);            
		        retries++;
		    }
		};
        
        function openWebSocket(){
		    if (notificationSocket.readyState === undefined || notificationSocket.readyState > 1) {
		        $scope.connect(account.login);
		    }
		}
        
        $scope.getNewNotifications = function(){
        	$http.get('api/notifications/allUnseen/' + $scope.userName).
        	success(function(data, status, headers, config) {
		      $scope.newNotifications = data;
		      $scope.notificationsCount = $scope.newNotifications.length;
		      //alert(JSON.stringify($scope.newNotifications));
		    });
        };
        
        
        Principal.identity().then(function(account) {
            if(account != null){
	        	$scope.connect(account.login);
	        	$scope.userName = account.login;
	        	$scope.getNewNotifications();
            	$scope.getCar();
	        }
            
            
        });
        
        $scope.$on('username', function(){
        	Principal.identity().then(function(account) {
                $scope.connect(account.login);
                $scope.userName = account.login;
                $scope.getNewNotifications();
                $scope.getCar();
            });
        });
        
        
    
	    $scope.disconnect = function() {
	        if (stompNotificationClient != null) {
	            stompNotificationClient.disconnect();
	        }
	        //setConnected(false);
	        console.log("Disconnected");
	    };
        
        $scope.getCar =  function(){
        	$http.get('/api/car/getUserCar/' + $scope.userName ).
        	success(function(data, status, headers, config) {
        		$scope.make = data.make;
        	  if($scope.make == ""){		    
        	  	$scope.isDriver = false;
        	  }
        	  else{
        	  	$scope.isDriver = true;
        	  }
        	})
        	.error(function(data, status, headers, config) {
        		// log error
        	});
        };

        $scope.logout = function () {
            Auth.logout();
            $scope.disconnect();
            $state.go('home');
        };
        
        $(document).ready(function()
		{
			$("#notificationLink").click(function()
			{
				$("#notificationContainer").fadeToggle(300);
				$("#notification_count").fadeOut("slow");
				return false;
			});
		
			//Document Click hiding the popup 
			$(document).click(function()
			{
				$("#notificationContainer").hide();
			});
		
			//Popup on click
			$("#notificationContainer").click(function()
			{
				return false;
			});
		
		});
		
		$scope.markAsSeen = function(){
			$http.get('/api/notifications/markSeen/' + $scope.userName ).
        	success(function(data, status, headers, config) {
        		$scope.notificationsCount = 0;
        	})
        	.error(function(data, status, headers, config) {
        		
        	});
		};
    });



  