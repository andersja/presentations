'use strict';

angular.module('harleystoreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('model', {
                parent: 'entity',
                url: '/models',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'harleystoreApp.model.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/model/models.html',
                        controller: 'ModelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('model');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('model.detail', {
                parent: 'entity',
                url: '/model/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'harleystoreApp.model.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/model/model-detail.html',
                        controller: 'ModelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('model');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Model', function($stateParams, Model) {
                        return Model.get({id : $stateParams.id});
                    }]
                }
            })
            .state('model.new', {
                parent: 'model',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/model/model-dialog.html',
                        controller: 'ModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    price: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('model', null, { reload: true });
                    }, function() {
                        $state.go('model');
                    })
                }]
            })
            .state('model.edit', {
                parent: 'model',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/model/model-dialog.html',
                        controller: 'ModelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Model', function(Model) {
                                return Model.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('model', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
