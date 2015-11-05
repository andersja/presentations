'use strict';

angular.module('harleystoreApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


