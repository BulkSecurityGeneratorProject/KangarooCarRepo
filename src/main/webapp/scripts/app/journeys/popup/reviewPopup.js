angular.module('carshareApp').controller('ReviewPopupController', function ($scope, $rootScope, $modal, $log, Principal, Auth) {
  $scope.template;
  $scope.max = 10;
	Principal.identity().then(function(account) {
	    $scope.settingsAccount = account;
	});
	
  	$scope.open = function (them, journeyId) {
	    $rootScope.me = $scope.settingsAccount.login;
	    $rootScope.them = them;
	    $rootScope.journeyId = journeyId;
	    var modalInstance = $modal.open({
	      animation: true,
	      backdrop: 'static',
	      keyboard: false,
	      templateUrl: 'scripts/app/journeys/popup/templates/review.html',
	      controller: 'ReviewPopupControllerInstance',
	      resolve: {
	        items: function () {
	          
	        }
	      }
	    });
	  };

	  $scope.hoveringOver = function(value) {
			$scope.overStar = value;
			$scope.percent = 100 * (value / $scope.max);
		};
});

angular.module('carshareApp').controller('ReviewPopupControllerInstance', function ($scope, $state, $rootScope, $modalInstance, items, Principal, Auth, $http) {
	$scope.bId = items;
	Principal.identity().then(function(account) {
	    $scope.settingsAccount = account;
	});
	$scope.them = $rootScope.them;
	$scope.me = $rootScope.me;
	$scope.journeyId = $rootScope.journeyId;
	
	$scope.ok = function () {
		$modalInstance.close();
	};

	$rootScope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

	$scope.sendReview = function(){
    	var reviewData = {
    		'senderUserName': $scope.me,
    		'receiverUserName': $scope.them,
    		'message': $scope.review,
    		'value': $scope.reviewValue,
    		'journeyId': $scope.journeyId
    		
    	};
    	$http.post('api/review/addReview',reviewData).
    	success(function(data, status, headers, config){
    		
    	}).
    	error(function(data, status, headers, config){
    		if(status == 400){
    			$scope.cantDo = true;
    		}
    	});
		$state.reload();
    };

	
});