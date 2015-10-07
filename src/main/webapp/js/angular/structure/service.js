'use strict';

angular.module('jarvis.structure.srv', [])
    .factory('structureSrv', structureSrv);

structureSrv.$inject = ['$http'];

function structureSrv($http) {
    //var PROJECT_API ="https://jarvis.subutai.io/services/api/timeline/project.json";
    //var STRUCTURE_API = "https://jarvis.subutai.io/services/api/timeline/project/"
    var PROJECT_API = "dummy-api/P.json";
    var STRUCTURE_API = "dummy-api/";
    var EVENT_API = 'dummy-api/';
    //this.metrics;
    //this.project;
    //var STRUCTURE_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project/";


    var structureSrv = {
        getProjects: getProjects,
        getIssues: getIssues,
        getEvents: getEvents
    };

    return structureSrv;

    //// Implementation

    function getProjects() {
        return $http.get(PROJECT_API, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }

    function getIssues(key) {
        return $http.get(STRUCTURE_API + key, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }

    function getEvents(key) {
        return $http.get(EVENT_API + key, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }	
}
