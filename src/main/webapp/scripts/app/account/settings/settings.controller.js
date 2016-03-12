'use strict';

angular.module('carshareApp').controller('SettingsController', function ($scope, Principal, Auth, $http) {
        $scope.success = null;
        $scope.successProfilePic = false;
        $scope.errorProfilePic = false;
        $scope.error = null;
        $scope.image = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = account;
            
            $scope.profilePic ={
	        	userName: $scope.settingsAccount.login,
	        	profilePicture : $scope.myFile
	        };
        });
        
        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        $scope.document = {};
        
        $scope.uploadFile=function(){
             var formData=new FormData();
	         formData.append("profilePicture",file.files[0]);
	         formData.append("userName",$scope.profilePic.userName);
            $.ajax({
	              url: '/user/profile/addPicture/',
	              data: formData,
	              processData: false,
	              contentType: false,
	              type: 'POST',
	              async: false,
	              success: function(data){
	            	  $scope.successProfilePic = true;
	            	  $scope.errorProfilePic = false;
	              },
	              error: function(){
	            	  $scope.successProfilePic = false;
	              	  $scope.errorProfilePic = true;
	              }
            });
            
      };
      
    $scope.readURL = function(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#profilePic')
                    .attr('src', e.target.result);
            };
            reader.readAsDataURL(input.files[0]);
        };
    };
    });
