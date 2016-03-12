'use strict';

angular.module('carshareApp')
	.controller('RegisterController', function ($scope, $state, $translate, Principal, $timeout, Auth, $http, $rootScope) {
		Principal.identity().then(function(account) {
	        $scope.account = account;
	        $scope.me = account.login;
	        $scope.registerAccount.login = $scope.me;
	    });	
		$scope.$on('username', function(){
	        Principal.identity().then(function(account) {
	            $scope.account = account;
	            $scope.me = account.login;
	            $scope.registerAccount.login = $scope.me;
	        });
    	});
		
		$scope.errorCapatcha =null;
        $scope.carSuccess = null;
        $scope.carError = null;
        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.errorUserExists = null;
        $scope.userType = "driver";
        $scope.registerAccount = {};

        $scope.car = {};
        
        $scope.isDriver = true; 
        $scope.years = [];
        $scope.currentYear = (new Date).getFullYear();
        $scope.minYear = 1960;
        
        for(var i = $scope.currentYear; i >= $scope.minYear; i--){
        	$scope.years.push(i);
        }
        
        
        $scope.getMakes = function(){
        	$http.get('/api/dropdown/makes').
        	success(function(data, status, headers, config){
        		$scope.makes = data.makes;
        	}).
        	error(function(data, status, headers, config) {
		    	// log error
		    });
        };
        
        $scope.getModels = function(make){
        	$http.get('/api/dropdown/models/' + make).
        	success(function(data, status, headers, config){
        		$scope.models = data.makeAndModel;
        	}).
        	error(function(data, status, headers, config) {
		    	// log error
		    });
        };
        $scope.getMakes();

        
        $timeout(function (){angular.element('[ng-model="registerAccount.login"]').focus();});
        
        $scope.registerCar = function(){
        	if($scope.userType == "driver")
        	{
        		var carData = {
        			'userName': $scope.registerAccount.login,
        			'makeAndModel': $scope.car.model,
        			'year':$scope.car.year
        		};
        		$http.post('/api/car/registration',carData).
	        	success(function(data, status, headers, config){
	        		$scope.carSuccess = true;
	        	}).
	        	error(function(data, status, headers, config){
	        		$scope.carError = true;
	        	});
        	}
        };
        $scope.addEditCar = function(){
        	$scope.registerCar();
        	$state.reload();
        	$rootScope.$broadcast('username');
        };
		
        $scope.register = function () {
            if ($scope.registerAccount.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.registerAccount.langKey = $translate.use();
                $scope.doNotMatch = null;
                $scope.error = null;
                $scope.errorUserExists = null;
                $scope.errorEmailExists = null;
                if(grecaptcha.getResponse() == ""){
                	$scope.errorCapatcha = true;
                }
                else{
                	$scope.errorCapatcha = null;
	                Auth.createAccount($scope.registerAccount).then(function () {
	                    $scope.success = 'OK';
	                    $scope.registerCar();
	                }).catch(function (response) {
	                    $scope.success = null;
	                    if (response.status === 400 && response.data === 'login already in use') {
	                        $scope.errorUserExists = 'ERROR';
	                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
	                        $scope.errorEmailExists = 'ERROR';
	                    } else {
	                        $scope.error = 'ERROR';
	                    }
	                });
	            }
            };
        };
        $('#radioBtn a').on('click', function(){
            var sel = $(this).data('title');
            var tog = $(this).data('toggle');
            $('#'+tog).prop('value', sel);
            
            $('a[data-toggle="'+tog+'"]').not('[data-title="'+sel+'"]').removeClass('active').addClass('notActive');
            $('a[data-toggle="'+tog+'"][data-title="'+sel+'"]').removeClass('notActive').addClass('active');
        });
    });
