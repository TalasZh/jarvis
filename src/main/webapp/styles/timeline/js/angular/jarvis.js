var app = angular.module("jarvis", [
    'ui.router',
    'ui.bootstrap',
    'oc.lazyLoad'
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
            templateUrl: "/styles/timeline/js/angular/structure/view.html",
            resolve: {
                loadPlugin: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: ['/styles/timeline/js/arbor/arbor.js']
                        },
                        {
                            name: 'jarvis.structure.srv',
                            files: ['/styles/timeline/js/angular/structure/service.js']
                        },
                        {
                            name: 'jarvis.structure.ctrl',
                            files: ['/styles/timeline/js/angular/structure/controller.js']
                        }
                    ]);
                }]
            }
        })
        .state("timeline", {
            url: "/timeline",
            templateUrl: "/styles/timeline/js/angular/timeline/view.html",
            resolve: {
                loadPlugin: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            files: [
                                '/styles/timeline/js/babylon/babylon.2.1.debug.js',
                                '/styles/timeline/js/event-listener.js',
                                '/styles/timeline/js/builder-timeline.js'
                            ]
                        },
                        {
                            name: 'jarvis.timeline.srv',
                            files: ['/styles/timeline/js/angular/timeline/service.js']
                        },
                        {
                            name: 'jarvis.timeline.ctrl',
                            files: ['/styles/timeline/js/angular/timeline/controller.js']
                        }
                    ])
                }]
            }
        });
}

function startup($rootScope, $state) {
    $rootScope.$state = $state;
}
