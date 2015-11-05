'use strict';

angular.module('harleystoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('family', {
                parent: 'entity',
                url: '/familys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'harleystoreApp.family.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/family/familys.html',
                        controller: 'FamilyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('family');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('family.detail', {
                parent: 'entity',
                url: '/family/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'harleystoreApp.family.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/family/family-detail.html',
                        controller: 'FamilyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('family');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Family', function($stateParams, Family) {
                        return Family.get({id : $stateParams.id});
                    }]
                }
            })
            .state('family.new', {
                parent: 'family',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/family/family-dialog.html',
                        controller: 'FamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('family', null, { reload: true });
                    }, function() {
                        $state.go('family');
                    })
                }]
            })
            .state('family.edit', {
                parent: 'family',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/family/family-dialog.html',
                        controller: 'FamilyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Family', function(Family) {
                                return Family.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('family', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
