var app = angular.module("jarvis", [
    'ui.router',
    'ui.bootstrap',
    'oc.lazyLoad',
    'ngAnimate',
    'ngStorage'
])
    .config(routesConf)
    .run(startup);

routesConf.$inject = ['$stateProvider', '$urlRouterProvider', '$ocLazyLoadProvider'];
startup.$inject = ['$rootScope', '$state'];

function routesConf($stateProvider, $urlRouterProvider, $ocLazyLoadProvider) {

    $urlRouterProvider.otherwise("/");

    $ocLazyLoadProvider.config({
        debug: false
    });

    $stateProvider
        .state("home", {
            url: "/",
            templateUrl: "js/angular/structure/view.html",
            resolve: {
                loadPlugin: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: ['js/arbor/arbor.js', 'js/builder-structure-popup.js', 'js/libs/slick.min.js']
                        },
                        {
                            name: 'ui.knob',
                            files: ['js/libs/addons/jquery.knob.min.js', 'js/libs/angular-knob.js']
                        },
                        {
                            name: 'jarvis.structure.srv',
                            files: ['js/angular/structure/service.js']
                        },
                        {
                            name: 'jarvis.structure.ctrl',
                            files: ['js/angular/structure/controller.js']
                        }
                    ]);
                }]
            }
        })
        .state("timeline", {
            url: "/timeline/{key}",
            templateUrl: "js/angular/timeline/view.html",
            resolve: {
                loadPlugin: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: [
                                'js/babylon/babylon.2.1.debug.js',
                                'js/event-listener.js',
                                'js/builder-popup.js',
                                'js/builder-timeline.js'
                            ]
                        },
                        {
                            name: 'jarvis.structure.srv',
                            files: ['js/angular/structure/service.js']
                        },
                        {
                            name: 'jarvis.timeline.srv',
                            files: ['js/angular/timeline/service.js']
                        },
                        {
                            name: 'jarvis.timeline.ctrl',
                            files: ['js/angular/timeline/controller.js']
                        }
                    ])
                }]
            }
        });
}

function startup($rootScope, $state) {
    $rootScope.$state = $state;
}
