angular.module('carshareApp')
    .controller('ContactPassengerController', function ($scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService) {
        $scope.contactFail = null;
        $scope.contactSuccess = null;
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.me = account.login;
        });
        
        $scope.addContactPassenger = function(){
        	var contactData = {
        		'journeyRequestId': UserService.journeyId,
    			'userName': $scope.me,
    			'message': $scope.message,
    			'journeyId': $scope.journeyId
			};
			$http.post('/api/contact/contactPassenger', contactData).
	    	success(function(data, status, headers, config){
	    		if(data.Status == "FAIL"){
	    			$scope.contactFail = 'OK';
	    		}
	    		else{
	    			$scope.contactSuccess = 'OK';
	    			$('#submitBid').attr("disabled", "disabled");
	    		}
	    	}).
	    	error(function(data, status, headers, config){
	    		$scope.contactFail = 'OK';
	    	});
	    };
    });