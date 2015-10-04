'use strict';

angular.module('jarvis.structure.srv', [])
    .factory('structureSrv', structureSrv);


structureSrv.$inject = ['$http'];

function structureSrv($http) {
	//var PROJECT_API ="http://jarvis-test.critical-factor.com:8080/services/api/timeline/project.json";
    var PROJECT_API = "/styles/timeline/dummy-api/P.json";
    var STRUCTURE_API = "/styles/timeline/dummy-api/";
    //var STRUCTURE_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project/";


    var structureSrv = {
        getProjects: getProjects,
        getIssues: getIssues
    };

    return structureSrv;

    //// Implementation

    function getProjects() {
        return $http.get(PROJECT_API, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }

    function getIssues(key) {
        return $http.get(STRUCTURE_API + key, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }
}
