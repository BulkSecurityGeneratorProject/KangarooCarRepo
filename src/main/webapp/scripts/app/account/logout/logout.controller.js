'use strict';

angular.module('carshareApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
