'use strict';

angular.module('carshareApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


