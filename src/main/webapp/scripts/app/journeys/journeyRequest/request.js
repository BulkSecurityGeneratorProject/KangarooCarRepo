'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('request', {
                parent: 'findRequests',
                url: '/request/:requestId',
                data: {
                    roles: [], 
                    pageTitle: 'journeyRequest.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/journeyRequest/request.html',
                        controller: 'RequestController'
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
