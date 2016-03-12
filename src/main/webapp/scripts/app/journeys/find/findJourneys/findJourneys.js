'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('findJourneys', {
                parent: 'site',
                url: '/findJourneys/:from?to?username?date',
                data: {
                    roles: [], 
                    pageTitle: 'findJourneys.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/find/findJourneys/findJourneys.html',
                        controller: 'FindJourneysController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('carshare');
                        return $translate.refresh();
                    }]
                }
            });
    });
