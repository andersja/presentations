'use strict';

describe('Model Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockModel, MockFamily;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockModel = jasmine.createSpy('MockModel');
        MockFamily = jasmine.createSpy('MockFamily');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Model': MockModel,
            'Family': MockFamily
        };
        createController = function() {
            $injector.get('$controller')("ModelDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'harleystoreApp:modelUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
