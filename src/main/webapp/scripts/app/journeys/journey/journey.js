'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('journey', {
                parent: 'findJourneys',
                url: '/journey/:journeyId',
                data: {
                    roles: [], 
                    pageTitle: 'journey.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/journey/journey.html',
                        controller: 'JourneyController'
                    }
                }
            });
    });
