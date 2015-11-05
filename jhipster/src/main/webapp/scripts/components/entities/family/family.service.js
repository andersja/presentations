'use strict';

angular.module('harleystoreApp')
    .factory('Family', function ($resource, DateUtils) {
        return $resource('api/familys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
