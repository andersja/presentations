 'use strict';

angular.module('harleystoreApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-harleystoreApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-harleystoreApp-params')});
                }
                return response;
            }
        };
    });
