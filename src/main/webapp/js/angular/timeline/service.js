'use strict';

angular.module('jarvis.timeline.srv', [])
    .factory('timelineSrv', timelineSrv);


timelineSrv.$inject = ['$http'];

function timelineSrv($http) {
    var EVENT_API = 'dummy-api/';
    //var GET_API = 'http://jarvis-test.critical-factor.com:8080/services/api/timeline/story/';

    var timelineSrv = {
        getEvents: getEvents
    };

    function getEvents( key )
    {
        return $http.get(EVENT_API + key, {withCredentials: true, headers: {'Content-Type': 'application/json'}});
    }

    return timelineSrv;
}