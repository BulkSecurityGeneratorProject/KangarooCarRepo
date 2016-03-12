'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider, $urlRouterProvider) {
       $stateProvider
        .state('addJourneyRequest', {
        	parent: 'site',
            url: '/addJourneyRequest',
            data: {
	              roles: ['ROLE_USER'],
	              pageTitle: 'addJourneyRequest.title'
	      	},
	      	views: {
	      		'content@': {
		            templateUrl: 'scripts/app/journeys/addJourneyRequest/formJR.html',
		            controller: 'AddJourneyRequestController'
	      		}
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carshare');
                    return $translate.refresh();
                }]
            }
        })        
         .state('addJourneyRequest.source', {
	    	   url: '/source',
	    	   views: {
	              'form-content@addJourneyRequest': {
	                  templateUrl: 'scripts/app/journeys/addJourneyRequest/addJourneyRequest.html',
			            controller: 'AddJourneyRequestController'
	              }
	           }
	      });                    
});
