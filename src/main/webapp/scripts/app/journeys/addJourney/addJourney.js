'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider, $urlRouterProvider) {
       $stateProvider
        .state('addJourney', {
        	parent: 'site',
            url: '/addJourney',
            data: {
	              roles: ['ROLE_USER'],
	              pageTitle: 'addJourney.title'
	      	},
	      	views: {
	      		'content@': {
		            templateUrl: 'scripts/app/journeys/addJourney/form.html',
		            controller: 'AddJourneyController'
	      		}
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carshare');
                    return $translate.refresh();
                }]
            }
        })        
         .state('addJourney.source', {
	    	  url: '/source',
	          views: {
	              'form-content@addJourney': {
	                  templateUrl: 'scripts/app/journeys/addJourney/addJourney.html',
			            controller: 'AddJourneyController'
	              }
	           }
	      })	       
	      .state('addJourney.additionalInfo', {
			   url: '/source/dateTime/additionalInfo',
			   views: {
		          'form-content@addJourney': {
		              templateUrl: 'scripts/app/journeys/addJourney/additionalInfo.html',
			            controller: 'AddJourneyController'
		          }
		       }
	      });  
        
});
