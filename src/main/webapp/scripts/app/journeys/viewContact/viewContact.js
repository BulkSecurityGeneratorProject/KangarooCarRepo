'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('viewContact', {
                parent: 'site',
                url: '/viewContact/:journeyRequestId',
                data: {
                    roles: [], 
                    pageTitle: 'viewContact.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/journeys/viewContact/viewContact.html',
                        controller: 'ViewContactController'
                    }
                }
            });
    });
