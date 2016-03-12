'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('manageBids', {
                parent: 'site',
                url: '/manageBids/:journeyId',
                data: {
                    roles: [], 
                    pageTitle: 'manageBids.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/manageBids/manageBids.html',
                        controller: 'ManageBidsController'
                    }
                }
            });
    });
