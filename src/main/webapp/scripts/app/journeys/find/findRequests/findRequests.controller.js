'use strict';

angular.module('carshareApp')
	.controller('FindRequestsController', function ($scope, Principal, $translate, $timeout, Auth, $http, $filter) {
		Principal.identity().then(function(account) {
			$scope.account = account;
			$scope.isAuthenticated = Principal.isAuthenticated;
		});
	
		$scope.showJourneyRequests = function(pageNumber){
			$http.get('/api/journey/allJourneyRequests?page=' + pageNumber).
			success(function(data, status, headers, config) {
				$scope.journeyRequests = data.content;
				$scope.currentPage = data.number + 1;
				$scope.numPerPage = data.size;
				$scope.total = data.totalElements;
				$scope.maxSize = 5;
			}).
			error(function(data, status, headers, config) {
				// log error
			});
		};
	
		$scope.showJourneyRequests(0);
	
		$scope.pageChanged = function(page){
			page--;
			$scope.showJourneyRequests(page);
			$('html, body').animate({ scrollTop: 0 }, 'medium');
		};
		//Search
		$scope.searching = false;
		$scope.filterBySource = "";
		$scope.filterByDestination = "";
		$scope.filterByUsername = "";
		$scope.filterByDate = "";
	
		$scope.search = function(page){
			var searchData = {
					'source': $scope.filterBySource,
					'destination': $scope.filterByDestination,
					'username': $scope.filterByUsername,
					'date': $filter('date')($scope.filterByDate, "dd/MM/yyyy")
			};
			$http.post('/api/journey/searchJourneyRequest?page='+page,searchData).
			success(function(data, status, headers, config){
				$scope.journeyRequests = data.content;
				$scope.noJourneys = $scope.journeys <= 0? true:false;
				$scope.currentPage = data.number + 1;
				$scope.numPerPage = data.size;
				$scope.total = data.totalElements;
				$scope.maxSize = 4;
			}).	
			error(function(data, status, headers, config){
	
			});
		};
	
		$scope.pageSearchChanged = function(page){
			page--;
			$scope.search(page);
			$('div, #userJourneyList').animate({ scrollTop: 0 }, 'medium');
		};
		$scope.open = false;
		$scope.toggle = function() {
			$scope.open = !$scope.open;
			if(!$scope.open){
				$scope.showJourneyRequests();
				$scope.searching = false;
			}
			$scope.clear();
		};
	
		$scope.clear = function() {
			$scope.filterBySource = "";
			$scope.filterByDestination = "";
			$scope.filterByUsername = "";
			$scope.filterByDate = "";
		};
		
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!
		var yyyy = today.getFullYear();

		if(dd<10) {
			dd='0'+dd;
		} 

		if(mm<10) {
			mm='0'+mm;
		}
		today = mm+'/'+dd+'/'+yyyy;
		$scope.today = new Date(today);
		$scope.showClosed = function(jDate){
			var date  = new Date(jDate);
			if(date.getTime() < $scope.today.getTime()){
				return true;
			}
			else{
				false;
			}
		};
});
