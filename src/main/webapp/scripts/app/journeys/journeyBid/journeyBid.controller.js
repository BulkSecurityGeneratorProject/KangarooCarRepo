angular.module('carshareApp')
    .controller('JourneyBidController', function ($rootScope,$scope, $stateParams, Principal, $translate, $timeout, Auth, $http, UserService) {
        $scope.bidExists = null;
        $scope.bidSaved = null;
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.me = account.login;
        });
        $scope.bid = $rootScope.suggestedCost;
        $scope.addJourneyBid = function(){
        	var bidData = {
        		'journeyId': UserService.journeyId,
    			'userName': $scope.me,
    			'bid': $scope.bid,
    			'pickupLocation': $scope.pickupLocation,
    			'dropOffLocation': $scope.dropOffLocation
    		};
    		$http.post('/api/journeyBid/registerJourneyBid/'+$scope.me,bidData).
        	success(function(data, status, headers, config){
        		if(data.Status == "FAIL"){
        			$scope.bidExists = 'OK';
        		}
        		else{
        			$scope.bidSaved = 'OK';
        			$('#submitBid').attr("disabled", "disabled");
        		}
        	}).
        	error(function(data, status, headers, config){
        		//$scope.error = true;
        	});
        };
    });