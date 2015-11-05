'use strict';

angular.module('harleystoreApp')
    .controller('FamilyController', function ($scope, Family, ParseLinks) {
        $scope.familys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Family.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.familys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.familys = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Family.get({id: id}, function(result) {
                $scope.family = result;
                $('#deleteFamilyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Family.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteFamilyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.family = {
                name: null,
                id: null
            };
        };
    });
