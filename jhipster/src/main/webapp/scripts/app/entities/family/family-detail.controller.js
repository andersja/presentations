'use strict';

angular.module('harleystoreApp')
    .controller('FamilyDetailController', function ($scope, $rootScope, $stateParams, entity, Family, Model) {
        $scope.family = entity;
        $scope.load = function (id) {
            Family.get({id: id}, function(result) {
                $scope.family = result;
            });
        };
        var unsubscribe = $rootScope.$on('harleystoreApp:familyUpdate', function(event, result) {
            $scope.family = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
