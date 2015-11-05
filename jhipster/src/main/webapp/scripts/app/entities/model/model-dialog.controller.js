'use strict';

angular.module('harleystoreApp').controller('ModelDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Model', 'Family',
        function($scope, $stateParams, $modalInstance, entity, Model, Family) {

        $scope.model = entity;
        $scope.familys = Family.query();
        $scope.load = function(id) {
            Model.get({id : id}, function(result) {
                $scope.model = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('harleystoreApp:modelUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.model.id != null) {
                Model.update($scope.model, onSaveFinished);
            } else {
                Model.save($scope.model, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
