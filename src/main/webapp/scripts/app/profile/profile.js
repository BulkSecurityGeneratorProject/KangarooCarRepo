'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profile', {
                parent: 'site',
                url: '/profile/:username',
                data: {
                    roles: [],
                    pageTitle: 'profile.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/profile/profile.html',
                        controller: 'ProfileController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('carshare');
                        return $translate.refresh();
                    }]
                }
            });
        
        $stateProvider.state('/one', {
            templateUrl: 'page1.html'
        })
            .state('/two', {
            templateUrl: 'page2.html'
        });
       
    });
