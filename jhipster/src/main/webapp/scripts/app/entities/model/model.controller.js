'use strict';

angular.module('harleystoreApp')
    .controller('ModelController', function ($scope, Model, ParseLinks) {
        $scope.models = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Model.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.models.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.models = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Model.get({id: id}, function(result) {
                $scope.model = result;
                $('#deleteModelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Model.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteModelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.model = {
                name: null,
                price: null,
                id: null
            };
        };
    });
