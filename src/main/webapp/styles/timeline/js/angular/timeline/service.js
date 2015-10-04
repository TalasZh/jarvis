'use strict';

angular.module('jarvis.timeline.srv', [])
    .factory('timelineSrv', structureSrv);


structureSrv.$inject = ['$http'];

function structureSrv($http) {
    var PROJECT_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project.json";
    //var PROJECT_API = "dummy-api/P.json";
    //var STRUCTURE_API = "dummy-api/AS.json";
    var STRUCTURE_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project/";

    var timelineSrv = {

    };

    return timelineSrv;
}