'use strict';

angular.module('carshareApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('contact', {
                parent: 'site',
                url: '/contact',
                data: {
                    roles: [], 
                    pageTitle: 'Contact Us'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/contact/contact.html',
                        controller: 'ContactController'
                    }
                }
            });
    });
