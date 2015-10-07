'use strict';

angular.module('jarvis.timeline.ctrl', [
])
    .controller('TimelineCtrl', TimelineCtrl);

TimelineCtrl.$inject = ['$stateParams', 'timelineSrv', '$scope', '$sessionStorage'];

function TimelineCtrl($stateParams, timelineSrv, $scope, $sessionStorage)
{
    var vm = this;
    vm.step = 24 * 3600;
    vm.baseUrl = 'subutai.io';
    $scope.$storage = $sessionStorage;

    console.log($stateParams.key);
    console.log($scope.$storage.project);
    console.log($scope.$storage.metrics);


    var canvas, engine;
    var popup;

    var DATA = [];
    vm.FILTERED_DATA = [];
    vm.description;
    vm.creationDate

    vm.activeNodeData = $scope.$storage.metrics;
    vm.storyList = [];
    vm.members = $scope.$storage.project.users;
    vm.issueTypes = [
		'Desigen', 'Requirement', 'Playbook', 'Bug', 'Research'
	];

    var builder;
    var eventListener;

    var STATUS_OPENED = "OPENED";
    var STATUS_CLOSED = "CLOSED";

    var leapCtrl = new Leap.Controller();

	//storyList array

    timelineSrv.getEvents('timeline.json').success(function (data) {
        var issues = data.issues;
		vm.description = data.description;
        vm.creationDate = data.creationDate;

		var currentIssue;
		for(var i = 0; i < $scope.$storage.project.issues.length; i++){
			for(var y = 0; y < $scope.$storage.project.issues[i].issues.length; y++) {
				var currentIssue = $scope.$storage.project.issues[i].issues[y];
				if(currentIssue.issueType == 'Story'){
					vm.storyList.push({"storyId": currentIssue.id, "storyKey": currentIssue.key});
				}
			}
		}

        if (issues == undefined) return;

        for (var i = 0; i < issues.length; i++) {
            var result = $.grep(DATA, function (e) {
                if(undefined !== e) {
                    return e.issueId == issues[i].issueId;
                }
            });

            if (result.length > 0) {
                mergeEvents(issues[i].issueId, issues[i].changelogList)
            }
            else {
                DATA.push(issues[i]);
            }

            updateStatuses(issues[i]);
        }

        for( var i = 0; i < DATA.length; i++ )
        {
            DATA[i].changelogList.sort(function(a, b){return a.changeKey.created - b.changeKey.created});
        }

        if (BABYLON.Engine.isSupported()) {
            initScene();
            initEvents();

            engine.runRenderLoop(function () {
                builder.scene.render();
                eventListener.update();
            });

            updateView();
        }
    });

    function mergeEvents(id, data) {
        for (var i = 0; i < data.length; i++) {
            var result = $.grep(DATA, function (e) {
                return e.changeKey.changeItemId == id;
            });

            if (result.length > 0) {
                result = result[0];

                for (var j = 0; j < result.changelogList.length; j++) {

                    if ( data[i].changelogList[j].changeKey.changeItemId != result.changelogList[j].changeKey.changeItemId ) {
                        result.changelogList.push(data[i]);
                    }
                }
            }
        }
    }

    function updateStatuses(data) {
        data.issueStatuses = [];

        var stat = {};
        stat.status = STATUS_OPENED;
        stat.date = data.creationDate;
        data.issueStatuses.push(stat);

        for (var i = 0; i < data.changelogList.length; i++) {
            if (data.changelogList[i].to == 'Reopend') {
                var stat = {};
                stat.status = STATUS_OPENED;
                stat.date = data.changelogList[i].eventDate;
                data.issueStatuses.push(stat);
            }

            if (data.changelogList[i].to == 'Close') {
                var stat = {};
                stat.status = STATUS_CLOSED;
                stat.date = data.changelogList[i].eventDate;
                data.issueStatuses.push(stat);
            }
        }
    }

// @todo rewrite
    function filterData() {
        vm.FILTERED_DATA = cloneArray(DATA);

        var name = null;

        var tmp = [];

        if( name != null ) {

            for( var i = 0; i < vm.FILTERED_DATA.length; i++ )
            {
                if ( vm.FILTERED_DATA[i].assigneeName == name ) {
                    tmp.push( vm.FILTERED_DATA[i] );
                }
            }
            vm.FILTERED_DATA = tmp;
        }


        var type = null;

        var tmp = [];

        if( type != null ) {
            for( var i = 0; i < vm.FILTERED_DATA.length; i++ )
            {
                if ( vm.FILTERED_DATA[i].type.name == type ) {
                    tmp.push( vm.FILTERED_DATA[i] );
                }
            }
            vm.FILTERED_DATA = tmp;
        }

    }

    function getZCoordinate(date) {
        if (date == 0) {
            return MAX_EVENTS;
        }

        var lastDate = builder.activeDate;
        var comparable = new Date(date);

        return (comparable - lastDate) / 1000 / 3600 / 24;
    }

    function cloneArray(data) {
        var newArr = [];

        for (var i = 0; i < data.length; i++) {
            newArr.push(data[i]);
        }

        return newArr;
    }

    function updateView() {


        filterData();
        builder.updateGrid(vm.FILTERED_DATA.length + 1);

        var issuePosition = 1;

        // @todo sorting
        for (var i = vm.FILTERED_DATA.length - 1; i >= 0; i--) {
            if (vm.FILTERED_DATA[i].lane == null) {
                vm.FILTERED_DATA[i].lane = issuePosition++;
            }
        }

        for (var i = 0; i < vm.FILTERED_DATA.length; i++) {
            createIssueLanes(vm.FILTERED_DATA[i]);
            for (var j = 0; j < vm.FILTERED_DATA[i].changelogList.length; j++) {

                var date = new Date(parseInt( vm.FILTERED_DATA[i].changelogList[j].changeKey.created / 1000 / vm.step) * vm.step * 1000);
                var position = parseInt( ((vm.FILTERED_DATA[i].changelogList[j].changeKey.created / 1000) % vm.step) / ( vm.step / 10 ) );

                if( vm.FILTERED_DATA[i].changelogList[j].field == "Link" ) {

                    var result = $.grep(DATA, function (e) {
                        return e.issueKey == vm.FILTERED_DATA[i].changelogList[j].to;
                    });

                    if( !result[0] ) continue;


                    // @todo build progress
                    var type = "child";

                    if( vm.FILTERED_DATA[i].changelogList[j].toString.indexOf( "blocker" ) > 0 )
                        type = "blocker";

                    if( vm.FILTERED_DATA[i].changelogList[j].toString.indexOf( "child" ) > 0 )
                        continue;

                    builder.createArc(
                        vm.FILTERED_DATA[i].lane,
                        getZCoordinate(new Date(vm.FILTERED_DATA[i].changelogList[j].changeKey.created)) * builder.vlaneInterval,
                        (result[0].lane - vm.FILTERED_DATA[i].lane),
                        type
                    );
                }
                else {
                    //@todo
                    builder.createEvent(
                        vm.FILTERED_DATA[i].changelogList[j].changeKey.changeItemId,
                        vm.FILTERED_DATA[i].issueId,
                        builder.vlanes[vm.FILTERED_DATA[i].lane].position.x,
                        getZCoordinate(new Date(vm.FILTERED_DATA[i].changelogList[j].changeKey.created)) * builder.vlaneInterval,
                        vm.FILTERED_DATA[i].changelogList[j].eventType,
                        vm.FILTERED_DATA[i].type,
                        popup
                    );
                }
            }
        }
    }

    function createIssueLanes(data) {
        var firstEventClosed = null;
        var hasIssues = false;
        for (var j = 0; j < data.issueStatuses.length; j++) {
            if (data.issueStatuses[j].status == STATUS_OPENED) {
                firstEventClosed = getZCoordinate(data.issueStatuses[j].date) * builder.vlaneInterval;
                hasIssues = true;
            }
            if (data.issueStatuses[j].status == STATUS_CLOSED) {
                var zPos = 0;
                var length = 0;

                if (firstEventClosed == null) {
                    zPos = 0;
                    length = getZCoordinate(data.issueStatuses[j].date) * builder.vlaneInterval;
                }
                else {
                    zPos = firstEventClosed;
                    length = getZCoordinate(data.issueStatuses[j].date) * builder.vlaneInterval - zPos;
                    firstEventClosed = null;
                }

                builder.createIssue(
                    data.id,
                    data.issueName,
                    data.lane,
                    zPos,
                    length,
                    data.type,
					popup
                );

                hasIssues = true;
            }
        }

        if (!hasIssues && data.status == STATUS_CLOSED) {
            return;
        }

        if (!hasIssues && data.status == STATUS_OPENED) {
            firstEventClosed = 0;
        }

        if (firstEventClosed != null) {
            var oneDay = 24*60*60*1000;
            var dateNow = new Date();
            var daysToNow = Math.round(Math.abs((dateNow.getTime() - builder.activeDate.getTime())/(oneDay)));
            builder.createIssue(
                data.issueId,
                data.issueKey,
                data.lane,
                firstEventClosed,
                daysToNow * builder.vlaneInterval - firstEventClosed,
                data.type.name,
				popup
            );
        }
    }

    var fetchDataRange = function( from, to ) {
        var buildData;
        for( var i = 0; i < vm.FILTERED_DATA.length; i++ )
        {
            var dev = {};
            for( var j = 0; j < vm.FILTERED_DATA[i].issueStatuses.length; j++ )
            {
                var cmpDate = vm.FILTERED_DATA[i].issueStatuses[j].date;
                if( from <= cmpDate && cmpDate <= to )
                {

                }
            }


            for( var j = 0; j < vm.FILTERED_DATA[i].changelogList.length; j++ )
            {
                var cmpDate = vm.FILTERED_DATA[i].changelogList[j].changeKey.created;
                if( from <= cmpDate && cmpDate <= to )
                {

                }
            }
        }
    };

    function initScene() {
        canvas = document.getElementById("renderCanvas");

        if (canvas === undefined || canvas === null) {
            return false;
        }

        engine = new BABYLON.Engine(canvas, true);

        builder = new Builder(engine, new Date( vm.creationDate ), new Date( vm.creationDate ), fetchDataRange);
        eventListener = new EventListener(builder, drawPointer);
        popup = new Popup(DATA, eventListener);

		canvas.addEventListener("PopupOpened", function(e) {
			eventListener.mouseUp();
		});		

        builder.createMaterials();
        builder.loadMeshes();
        builder.createBaseScene();
        eventListener.addLeapMotion( leapCtrl );
        eventListener.addMouseListener( document.getElementById("renderCanvas") );
        leapCtrl.connect();

        //builder.camera.attachControl(canvas, false);
    }

    function drawPointer(frame) {

        if( frame == null ) {
            $('.hand_parts').css( 'display', 'none' );
            $('#palm').css( 'display', 'none' );
        }

        $('.hand_parts').css( 'display', 'block' );
        $('#palm').css( 'display', 'block' );

        var hand = frame.hands[0];

        var appWidth = window.innerWidth;
        var appHeight = window.innerHeight;

        var iBox = frame.interactionBox;
        var normalizedPoint = iBox.normalizePoint(hand.indexFinger.stabilizedTipPosition, true);

        var appX = normalizedPoint[0] * appWidth;
        var appY = (1 - normalizedPoint[1]) * appHeight;


        $('#index_tip').css("left", appX);
        $('#index_tip').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.indexFinger.bones[1].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#index_mid').css("left", appX);
        $('#index_mid').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.indexFinger.bones[2].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;

        $('#index_bot').css("left", appX);
        $('#index_bot').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.middleFinger.bones[1].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#middle_tip').css("left", appX);
        $('#middle_tip').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.middleFinger.bones[2].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#middle_mid').css("left", appX);
        $('#middle_mid').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.middleFinger.bones[3].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;

        $('#middle_bot').css("left", appX);
        $('#middle_bot').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.ringFinger.bones[1].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#ring_tip').css("left", appX);
        $('#ring_tip').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.ringFinger.bones[2].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#ring_mid').css("left", appX);
        $('#ring_mid').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.ringFinger.bones[3].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;

        $('#ring_bot').css("left", appX);
        $('#ring_bot').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.pinky.bones[1].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#pinky_tip').css("left", appX);
        $('#pinky_tip').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.pinky.bones[2].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#pinky_mid').css("left", appX);
        $('#pinky_mid').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.pinky.bones[3].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;

        $('#pinky_bot').css("left", appX);
        $('#pinky_bot').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.thumb.bones[1].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#thumb_tip').css("left", appX);
        $('#thumb_tip').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.thumb.bones[2].nextJoint, true);

        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#thumb_bot').css("left", appX);
        $('#thumb_bot').css("top", appY);


        normalizedPoint = iBox.normalizePoint(hand.palmPosition, true);
        appX = normalizedPoint[0] * appWidth;
        appY = (1 - normalizedPoint[1]) * appHeight;


        $('#palm').css("left", appX);
        $('#palm').css("top", appY);
    }

    function initEvents() {
        $("#menu-toggle").click(function (e) {
            e.preventDefault();
            $("#wrapper-left").toggleClass("active-left");
            $(".profile").toggleClass("active-profile");
            $(".circle-avatar").toggleClass("active-avatar");
            $(".icons-service").toggleClass("active-icon");
            $(".show-text").toggleClass("hide-text");
            $(".si-icon").toggleClass("si-active");
            $(".canvas-conf").toggleClass("canvas-l-active");
            $(".icon-collapse").toggleClass("rotate180");

            var val = -100;
            if ($("#wrapper-left.active-left").length > 0) {
                val = 100;
            }
        });
        $(".menu-toggle-right").click(function (e) {
            e.preventDefault();
            $(".wrapper-right").toggleClass("active-right");
            $(".right-nav").toggleClass("active-rnav");
            $(".si-wide").toggleClass("short");
            $(".ng-isolate-scope").toggleClass("hide");
            $(".hide-icon").toggleClass("show");
            $(".icon-collapse-right").toggleClass("hide");
            $(".toggle-right a").toggleClass("toggle-active");

            var val = -301;
            if ($(".wrapper-right.active-right").length > 0) {
                val = 301;
            }
        });
    }
}
