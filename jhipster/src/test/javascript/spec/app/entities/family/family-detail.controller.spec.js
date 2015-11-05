'use strict';

describe('Family Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockFamily, MockModel;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockFamily = jasmine.createSpy('MockFamily');
        MockModel = jasmine.createSpy('MockModel');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Family': MockFamily,
            'Model': MockModel
        };
        createController = function() {
            $injector.get('$controller')("FamilyDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'harleystoreApp:familyUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
