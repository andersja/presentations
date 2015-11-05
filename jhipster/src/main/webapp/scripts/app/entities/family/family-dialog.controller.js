'use strict';

angular.module('harleystoreApp').controller('FamilyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Family', 'Model',
        function($scope, $stateParams, $modalInstance, entity, Family, Model) {

        $scope.family = entity;
        $scope.models = Model.query();
        $scope.load = function(id) {
            Family.get({id : id}, function(result) {
                $scope.family = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('harleystoreApp:familyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.family.id != null) {
                Family.update($scope.family, onSaveFinished);
            } else {
                Family.save($scope.family, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
