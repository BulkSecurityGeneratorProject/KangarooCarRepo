angular.module('carshareApp').controller('ModalDemoCtrl', function ($scope, $modal, $log, Principal, Auth) {
	$scope.template;

	Principal.identity().then(function(account) {
		$scope.settingsAccount = account;
	});

	$scope.open = function (popupTemplate) {
		$scope.me = $scope.settingsAccount.login;
		$scope.template = popupTemplate;
		var modalInstance = $modal.open({
			animation: true,
			backdrop: 'static',
			keyboard: false,
			templateUrl: 'scripts/app/journeys/popup/templates/' + $scope.template,
			controller: 'ModalInstanceCtrl',
			resolve: {
				items: function () {
					return $scope.bidId;
				}
			}
		});
	};

});

angular.module('carshareApp').controller('ModalInstanceCtrl', function ($scope, $rootScope, $modalInstance, items, Principal, Auth, $http) {
	$scope.bId = items;
	Principal.identity().then(function(account) {
	    $scope.settingsAccount = account;
	});
	
	$scope.getMyJourneys = function(){
		$http.get('/api/journey/allJourneys/' + $scope.settingsAccount.login).
	    success(function(data, status, headers, config) {
	      $scope.journeys = data;
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    });
	};

	$scope.ok = function () {
		$modalInstance.close();
	};

	$rootScope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};

});