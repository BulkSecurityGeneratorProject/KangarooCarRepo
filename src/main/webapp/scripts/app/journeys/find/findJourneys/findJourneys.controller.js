'use strict';

angular.module('carshareApp')
    .controller('FindJourneysController',function ($scope, Principal, $stateParams, $translate, $timeout, Auth, $http, $state, $log, $filter) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        
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
        var from = $stateParams.from;
    	
    	var to = $stateParams.to;
    	var username = $stateParams.username;
    	var date = $stateParams.date;
    	$scope.filterBySource = from;
        
        $scope.showJourneys = function(pageNumber){

        	$http.get('/api/journey/allRecentJourneys?page=' + pageNumber).//http://fsgw71589a:9000

		    success(function(data, status, headers, config) {
		      $scope.journeys = data.content;
		      $scope.noJourneys = $scope.journeys.length <= 0? true:false;
		      $scope.currentPage = data.number + 1;
			  $scope.numPerPage = data.size;
			  $scope.total = data.totalElements;
			  $scope.maxSize = 5;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    });
        };        
        

        $scope.pageChanged = function(page){
        	page--;
        	$scope.showJourneys(page);
        	$('html, body').animate({ scrollTop: 0 }, 'medium');	
        };
        
        $scope.Alert = function() {
			alert();
		};
		
		$scope.oneAtATime = true;

		  $scope.groups = [
		    {
		      title: 'Dynamic Group Header - 1',
		      content: 'Dynamic Group Body - 1'
		    },
		    {
		      title: 'Dynamic Group Header - 2',
		      content: 'Dynamic Group Body - 2'
		    }
		  ];

		  $scope.items = ['Item 1', 'Item 2', 'Item 3'];

		  $scope.addItem = function() {
		    var newItemNo = $scope.items.length + 1;
		    $scope.items.push('Item ' + newItemNo);
		  };

		  $scope.status = {
		    isFirstOpen: true,
		    isFirstDisabled: false
		  };
		  
		//Search
	    $scope.searching = false;
	    $scope.filterBySource = typeof from != 'undefined'? from: "";
	    $scope.filterByDestination = typeof to != 'undefined'? to: "";
	    $scope.filterByUsername = typeof username != 'undefined'? username: "";
	    $scope.filterByDate = typeof date != 'undefined'? date: "";
		    
	    $scope.search = function(page){
	    	var searchData = {
	    		'source': $scope.filterBySource,
	    		'destination': $scope.filterByDestination,
	    		'username': $scope.filterByUsername,
	    		'date': Date.parse($scope.filterByDate)//new Date($filter('date')($scope.filterByDate, "yyyy-MM-dd")).getTime()
	    	};	 
	    	$http.post('/api/journey/searchJourney?page='+page,searchData).
	    	success(function(data, status, headers, config){
	    		$scope.journeys = data.content;
	    		$scope.noJourneys = $scope.journeys.length <= 0? true:false;
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
          
          $('html, body').animate({ scrollTop: 0 }, 'medium');
	  };
	  $scope.open = false;
	  $scope.toggle = function() {
		  $scope.open = !$scope.open;
		  if(!$scope.open){
			  $scope.showJourneys();
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
	  
	  
	  //decide which tab to show
        if($scope.filterByDestination == "" && $scope.filterBySource == "" && $scope.filterByUsername == "" && $scope.filterByDate == ""){
        	$scope.showJourneys(0);
        }
        else{
        	$scope.search(0);
        	$scope.searching = true;
        }
        
        $scope.showCost = function(seatsLeft, jDate){
        	var date  = new Date(jDate);
        	if(seatsLeft > 0 && date.getTime() >= $scope.today.getTime()){
        		return true;
        	}
        	else{
        		false;
        	}
        };
        
        $scope.showFull = function(seatsLeft, jDate){
        	var date  = new Date(jDate);
        	if(!seatsLeft > 0 && date.getTime() >= $scope.today.getTime()){
        		return true;
        	}
        	else{
        		false;
        	}
        };
        
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

