'use strict';

angular.module('harleystoreApp')
    .controller('ModelDetailController', function ($scope, $rootScope, $stateParams, entity, Model, Family) {
        $scope.model = entity;
        $scope.load = function (id) {
            Model.get({id: id}, function(result) {
                $scope.model = result;
            });
        };
        var unsubscribe = $rootScope.$on('harleystoreApp:modelUpdate', function(event, result) {
            $scope.model = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
