var app = angular.module("jarvis", [
    'ui.router',
    'ui.bootstrap',
    'oc.lazyLoad',
    'ngAnimate'
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
            templateUrl: "js/angular/structureview/view.html",
            resolve: {
                loadPlugin: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: ['js/builder-structure-popup.js', 'js/libs/slick.min.js', 'js/libs/addons/moment.min.js']
                        },
                        {
                            name: 'ui.knob',
                            files: ['js/libs/addons/jquery.knob.min.js', 'js/libs/angular-knob.js']
                        },
                        {
                            name: 'jarvis.structure.srv',
                            files: ['js/angular/structureview/service.js']
                        },
                        {
                            name: 'jarvis.structure.ctrl',
                            files: ['js/angular/structureview/controller.js']
                        }
                    ]);
                }]
            }
        })
        .state("timeline", {
            url: "/s",
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
        });
}

function startup($rootScope, $state) {
    $rootScope.$state = $state;
}
