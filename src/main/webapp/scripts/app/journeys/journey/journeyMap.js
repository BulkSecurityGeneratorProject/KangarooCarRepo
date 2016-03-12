'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('journeyMap', {
                parent: 'journey',
                url: '/map',
                data: {
                    roles: [], 
                    pageTitle: 'Map'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/journey/journeyMap.html',
                        controller: 'JourneyController'
                    }
                }
            });
    });
