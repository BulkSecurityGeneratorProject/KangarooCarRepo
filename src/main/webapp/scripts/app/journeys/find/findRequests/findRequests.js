'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('findRequests', {
                parent: 'site',
                url: '/findRequests',
                data: {
                    roles: [], 
                    pageTitle: 'findRequests.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/find/findRequests/findRequests.html',
                        controller: 'FindRequestsController'
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
