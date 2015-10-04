'use strict';

angular.module('jarvis.timeline.ctrl', [
    'jarvis.timeline.srv'
])
    .controller('TimelineCtrl', TimelineCtrl);

TimelineCtrl.$inject = ['$rootScope', 'timelineSrv'];

function TimelineCtrl($rootScope, timelineSrv)
{
    var vm = this;

    var canvas, engine, scene;

    var GET_API = 'styles/timeline/dummy-api/timeline.json';
//var GET_API = 'http://jarvis-test.critical-factor.com:8080/services/api/timeline/story/';

    var DATA = [];
    var FILTERED_DATA = [];

    var appX = 0;
    var appY = 0;

    var objX = -1;
    var objY = -1;

    var builder;
    var eventListener;

    var addedToMenu = [];

    var STATUS_OPENED = "OPENED";
    var STATUS_CLOSED = "CLOSED";

    var leapCtrl = new Leap.Controller();

    var i = 0;


    if (BABYLON.Engine.isSupported()) {
        initScene();

        engine.runRenderLoop(function () {
            scene.render();
            eventListener.update();
        });

        var key = window.location.search.substring(1);

        getData( key, updateView );
    }


    function getData(key, callback) {
        $.ajax({
            url: GET_API, //  + key + "?from=1411891890000&to=1443427890000"
            async: false,
            dataType: 'json',
            xhrFields: {
                withCredentials: true
            },
            success: function (data) {
                var issues = data.issues;

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
                console.log(DATA);

                callback();
            },
            error: function (data) {
                console.log("ERROR" + data.statusText);
            }
        });
    }

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
    function modifyFilteredData(DATA) {
        var slices = [];
        var dateSlices = [];
        var eventSlice = [];
        for (var d = 0; d < 10; d++) {
            dateSlices[d] = 24 * 3600 / 10 * d;
        }
        for (var i = 0; i < DATA.length; i++) {
            slices[i] = DATA[i].changelogList;
            for (var w = 0; w < slices[i].length; w++) {
                if (slices[i][w].eventDate) {
                    var array = slices[i][w].eventDate.split("T")
                    var times = array[1].split(":");
                    var hours = times[0];
                    var minutes = times[1];
                    var seconds = times[2];
                    seconds = parseInt(seconds, 10) + (parseInt(minutes, 10) * 60 + (parseInt(hours, 10) * 3600));
                    for (var f = 0; f < 9; f++) {
                        if (seconds > dateSlices[f] && seconds < dateSlices[f + 1]) {
                            if (!eventSlice[array[0] + "T" + convertHMS(dateSlices[f]) + "id" + DATA[i].id]) {
                                eventSlice[array[0] + "T" + convertHMS(dateSlices[f]) + "id" + DATA[i].id] = [];
                            }
                            eventSlice[array[0] + "T" + convertHMS(dateSlices[f]) + "id" + DATA[i].id].push((convertHMS(seconds)));
                        }
                    }

                }
            }
            DATA[i].changelogList = eventSlice;
            eventSlice = [];
        }
        console.log(DATA);
        return DATA;
    }

// @todo rewrite
    function filterData() {
        //var value = $('#member-dropdown ').val();
        FILTERED_DATA = cloneArray(DATA);
        /*for (var i = 0; i < DATA.length; i++) {

         for (var j = 0; j < DATA[i].changelogList.length; j++) {

         if (DATA[i].changelogList[j].author == value) {
         var result = $.grep(FILTERED_DATA, function (e) {
         return e.id == DATA[i].id;
         });

         if (result.length == 0) {
         var obj = jQuery.extend({}, DATA[i]);
         obj.changelogList = [];
         obj.lane = null;
         FILTERED_DATA.push(obj);

         result = $.grep(FILTERED_DATA, function (e) {
         return e.id == DATA[i].id;
         });
         }

         result = result[0];

         result.changelogList.push(DATA[i].changelogList[j]);
         }
         }
         }*/
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
        builder.updateGrid(FILTERED_DATA.length + 1);

        var issuePosition = 1;

        // @todo sorting
        for (var i = FILTERED_DATA.length - 1; i >= 0; i--) {
            if (FILTERED_DATA[i].lane == null) {
                FILTERED_DATA[i].lane = issuePosition++;
            }
        }

        for (var i = 0; i < FILTERED_DATA.length; i++) {
            createIssueLanes(FILTERED_DATA[i]);
            for (var j = 0; j < FILTERED_DATA[i].changelogList.length; j++) {
                if( FILTERED_DATA[i].changelogList[j].field == "Link" ) {

                    var result = $.grep(DATA, function (e) {
                        return e.issueKey == FILTERED_DATA[i].changelogList[j].to;
                    });

                    if( !result[0] ) continue;

                    var type = "child";
                    console.log(result.toString);
                    //if( result['toString'].indexOf( "blocker" ) > 0 ) {
                    //    type = "blocker"
                    //}

                    console.log( FILTERED_DATA[i].issueKey, FILTERED_DATA[i].lane, builder.vlanes[FILTERED_DATA[i].lane].position.x );

                    builder.createArc(
                        FILTERED_DATA[i].lane,
                        getZCoordinate(new Date(FILTERED_DATA[i].changelogList[j].changeKey.created)) * builder.vlaneInterval,
                        (result[0].lane - FILTERED_DATA[i].lane),
                        type
                    );
                }
                else {
                    builder.createEvent(
                        FILTERED_DATA[i].changelogList[j].changeKey.changeItemId,
                        FILTERED_DATA[i].issueId,
                        builder.vlanes[FILTERED_DATA[i].lane].position.x,
                        getZCoordinate(new Date(FILTERED_DATA[i].changelogList[j].changeKey.created)) * builder.vlaneInterval,
                        FILTERED_DATA[i].changelogList[j].eventType,
                        FILTERED_DATA[i].type, FILTERED_DATA[i].changelogList[j].eventInfo
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
                    data.type
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
                data.type.name
            );
        }
    }


    function initScene() {
        canvas = document.getElementById("renderCanvas");

        if (canvas === undefined || canvas === null) {
            return false;
        }

        engine = new BABYLON.Engine(canvas, true);
        scene = new BABYLON.Scene(engine);

        builder = new Builder(engine, scene, new Date("2015-09-22"), new Date("2015-09-22"));
        eventListener = new EventListener(builder, drawPointer);


        builder.createMaterials();
        builder.loadMeshes();
        builder.createBaseScene();
        eventListener.addLeapMotion( leapCtrl );
        //eventListener.addMouseListener( document.getElementById("renderCanvas") );
        leapCtrl.connect();

        builder.camera.attachControl(canvas, false);
    }

    function drawPointer(appX, appY) {
        $('#position').css("left", appX);
        $('#position').css("top", appY);
    }

    function convertHMS(sec) {
        Number.prototype.toDecimals = function (n) {
            n = (isNaN(n)) ?
                2 :
                n;
            var
                nT = Math.pow(10, n);

            function pad(s) {
                s = s || '.';
                return (s.length > n) ?
                    s :
                    pad(s + '0');
            }

            return (isNaN(this)) ?
                this :
                (new String(
                    Math.round(this * nT) / nT
                )).replace(/(\.\d*)?$/, pad);
        };
        if (sec > 59) {
            var hrs = sec / 3600;
            if (hrs < 0) {
                hrs = "00";
                var min = hrs * 60;
                min = min.toDecimals(8);
                var snd = min.substring(min.indexOf('.'), min.length);
                min = min.substring('0', min.indexOf('.'));

                if (min < 10) {
                    min = '0' + min
                }
                snd = Math.round(snd * 60);
                if (snd < 10) {
                    snd = '0' + snd;
                }
                var tm = hrs + ':' + min + ':' + snd;
            }
            else {

                hrs = hrs.toDecimals(8);
                var min = hrs.substring(hrs.indexOf('.'), hrs.length)

                hrs = hrs.substring('0', hrs.indexOf('.'));

                if (hrs < 10) {
                    hrs = '0' + hrs;
                }
                min = min * 60
                min = min.toDecimals(8);
                var snd = min.substring(min.indexOf('.'), min.length);
                min = min.substring('0', min.indexOf('.'));

                if (min < 10) {
                    min = '0' + min
                }
                snd = Math.round(snd * 60);
                if (snd < 10) {
                    snd = '0' + snd;
                }
                var tm = hrs + ':' + min + ':' + snd;
            }
        }
        else {
            if (sec < 10) {
                sec = "0" + sec;
            }
            var tm = "00:00:" + sec
        }
        return tm;
    }



    function popup(mesh, text) {
        if (!MOVE_CAMERA) {
            mesh.material.alpha = 1;
            $('#popup').text(text);
            $('#popup').show();
            $('#popup').css("top", ( window.event.clientY - 110 ) + "px");
            $('#popup').css("left", (window.event.clientX - 125) + "px");
            doMouseUp();
        }
    }

    function popupHide(mesh) {
        mesh.material.alpha = 0.5;
        $('#popup').hide();
    }

    var SCREEN_WIDTH = window.innerWidth;
    var SCREEN_HEIGHT = window.innerHeight;
    var Z_INDEX = 10000;


    var selectedData;
    var selected_doc = 0;

    var left = parseInt(SCREEN_WIDTH / 6);
    var top_offset = parseInt(SCREEN_HEIGHT / 6);
    var width_diff = 15;
    var height_diff = 30;

    function getHighlightedStringInText(url, searchText, id) {
        var readabilityUrl = 'https://www.readability.com/api/content/v1/parser?url=' + url + '&token=0626cdbea9b15ece0ad7d9ee4af00c7a6fd13b40&callback=?'
        return $.getJSON(readabilityUrl).then(function(data){
            var contentText = data.content.replace(searchText, '<span class="b-highlight-string">'+searchText+'</span>');
            contentText = contentText.replace(/<img[^>]*>/g, "");
            contentText = contentText.replace('id="content"', '');
            var responseContentDiv = $('.js-content-for-' + id);
            responseContentDiv.html(contentText);
            var topScroll = responseContentDiv.find('.b-highlight-string').offset().top - responseContentDiv.offset().top;
            console.log(topScroll);
            responseContentDiv.scrollTop(topScroll);
        });
    }

    function showMinimap(id, eventId) {

        left = parseInt(SCREEN_WIDTH / 6);
        top_offset = parseInt(SCREEN_HEIGHT / 6);

        var popupLimit = 4;


        doMouseUp();
        var strBuilder;

        var utmost = false;

        var data = $.grep(DATA, function (e) {
            return e.issueId == id;
        });
        data = data[0];

        selectedData = data;

        for (var i = 0; i < data.changelogList.length && popupLimit > 0; i++) {
            var width_diff = ( 5 - popupLimit ) * 15;
            var height_diff = ( 5 - popupLimit ) * 30;

            /*var dateToDateFormat = new Date(data.changelogList[i].changeKey.created);
             var showDate = dateToDateFormat.getDay() + '.' + dateToDateFormat.getMonth() + '.' dateToDateFormat.getYear();*/

            if (data.changelogList[i].changeKey.changeItemId == eventId) {
                var popupData = [
                    (left * 4), (top_offset * 4), left, top_offset, Z_INDEX--,
                    data.issueKey, data.changelogList[i].changeKey.created, data.changelogList[i].issueKey, i, i
                ];
                strBuilder = vsprintf('<div style="width: %dpx; height: %dpx;  left: %d' +
                    'px; top: %dpx; z-index: %d;" class="document-lister">' +
                    '<table><tr class="task"><td style="width: 25px"><img src="styles/timeline/assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>TASK</td><td style="width: 30px"><img src="styles/timeline/assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
                    '<td>%s</td><td style="width: 30px"></td><td>%s</td></tr></table><div class="divider"></div>' +
                    '<div class="task-info">%s</div>' +
                    '<div class="b-request-text js-content-for-%d"></div>' +
                    '<script>getHighlightedStringInText(' +
                    '"https://paul.kinlan.me/rise-of-the-meta-platforms/",' +
                    '"Every single platform", %d);' +
                    '</script>' +
                    '<button type="button" class="btn btn-default btn-close"></button></div>', popupData);

                $('body').append(strBuilder);
                utmost = true;
            }
            else if (utmost && popupLimit-- >= 0) {
                var popupData = [
                    (left * 4 - width_diff * 2), (top_offset * 4), (left + width_diff), ( top_offset - height_diff ), Z_INDEX--,
                    data.issueKey, data.changelogList[i].changeKey.created, data.changelogList[i].issueKey, i, i
                ];
                strBuilder = vsprintf('<div style="width: %dpx; height: %dpx;  left: ' +
                    '%dpx; top: %dpx; z-index: %d;" class="document-lister">' +
                    '<table><tr class="task"><td style="width: 25px"><img src="styles/timeline/assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>TASK</td><td style="width: 30px"><img src="styles/timeline/assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
                    '<td>%s</td><td style="width: 30px"></td><td>%s</td></tr></table><div class="divider"></div>' +
                    '<div class="task-info">%s</div>' +
                    '<div class="b-request-text js-content-for-%d"></div>' +
                    '<script>getHighlightedStringInText(' +
                    '"https://paul.kinlan.me/rise-of-the-meta-platforms/",' +
                    '"Every single platform", %d);' +
                    '</script>' +
                    '<button type="button" class="btn btn-default btn-close"></button></div>', popupData);

                $('body').append(strBuilder);
            }
        }
    }

    function scrollResearch(mvmnt) {
        if ($('.document-lister').length == 0) {
            return;
        }

        var index_lowest = 1000000;
        var index_highest = 0;
        var top_doc;
        var bottom_doc;

        $('.document-lister').each(function () {
            var index_current = parseInt($(this).css("zIndex"));

            if (index_current > index_highest) {
                index_highest = index_current;
                top_doc = $(this);
            }

            if (index_current < index_lowest) {
                index_lowest = index_current;
                bottom_doc = $(this);
            }
        });

        if (mvmnt > 0) {

            if (selected_doc + 1 == selectedData.changelogList.length) {
                return;
            }
            selected_doc++;

            top_doc.remove();

            $('.document-lister').animate({
                top: '+=' + height_diff + 'px',
                left: '-=' + width_diff + 'px',
                width: '+=' + width_diff * 2 + 'px'
            });

            Z_INDEX = index_lowest - 1;

            if (selected_doc + 4 < selectedData.changelogList.length) {
                var popupData = [
                    (left * 4 - width_diff * 2), (top_offset * 4), (left + width_diff), ( top_offset - height_diff ), Z_INDEX--,
                    selectedData.issueKey, selectedData.changelogList[i].changeKey.created, selectedData.changelogList[i].issueKey, i, i
                ];
                var strBuilder = vsprintf('<div style="width: %dpx; height: %dpx;  left: ' +
                    '%dpx; top: %dpx; z-index: %d;" class="document-lister">' +
                    '<table><tr class="task"><td style="width: 25px"><img src="styles/timeline/assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>TASK</td><td style="width: 30px"><img src="styles/timeline/assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                    '<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
                    '<td>%s</td><td style="width: 30px"></td><td>%s</td></tr></table><div class="divider"></div>' +
                    '<div class="task-info">%s</div>' +
                    '<div class="b-request-text js-content-for-%d"></div>' +
                    '<script>getHighlightedStringInText(' +
                    '"https://paul.kinlan.me/rise-of-the-meta-platforms/",' +
                    '"Every single platform", %d);' +
                    '</script>' +
                    '<button type="button" class="btn btn-default btn-close"></button></div>', popupData);

                $('body').append(strBuilder);
            }
        }
        else {
            if (selected_doc - 1 < 0) {
                return;
            }
            selected_doc--;

            if ($('.document-lister').length >= 5) {
                bottom_doc.remove();
            }

            $('.document-lister').animate({
                top: '-=' + height_diff + 'px',
                left: '+=' + width_diff + 'px',
                width: '-=' + width_diff * 2 + 'px'
            });

            Z_INDEX = index_highest + 1;

            var popupData = [
                (left * 4), (top_offset * 4), left, top_offset, Z_INDEX--,
                selectedData.issueKey, selectedData.changelogList[i].changeKey.created, selectedData.changelogList[i].issueKey, i, i
            ];
            var strBuilder = vsprintf('<div style="width: %dpx; height: %dpx;  left: %d' +
                'px; top: %dpx; z-index: %d;" class="document-lister">' +
                '<table><tr class="task"><td style="width: 25px"><img src="styles/timeline/assets/img/icon-task.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                '<td>TASK</td><td style="width: 30px"><img src="styles/timeline/assets/img/icon-time.svg" alt=""height="25px" style="position: absolute; top: 18px"/></td>' +
                '<td>DATE</td></tr><tr class="task-name"><td style="width: 25px"></td>' +
                '<td>%s</td><td style="width: 30px"></td><td>%s</td></tr></table><div class="divider"></div>' +
                '<div class="task-info">%s</div>' +
                '<div class="b-request-text js-content-for-%d"></div>' +
                '<script>getHighlightedStringInText(' +
                '"https://paul.kinlan.me/rise-of-the-meta-platforms/",' +
                '"Every single platform", %d);' +
                '</script>' +
                '<button type="button" class="btn btn-default btn-close"></button></div>', popupData);

            $('body').append(strBuilder);
        }
    }

    $(document).on('click', 'button.btn-close', function () {
        Z_INDEX = 10000;
        $('.document-lister').remove();
    });

    var elem = document;
    if (elem.addEventListener) {
        if ('onwheel' in document) {
            elem.addEventListener("wheel", onWheel);
        }
    }

    function onWheel(e) {
        e = e || window.event;

        var delta = e.deltaY || e.detail || e.wheelDelta;

        if ($(event.target).closest('.document-lister').length > 0 && $(event.target).closest('.b-request-text').length === 0) {
            if(delta > 0) {
                scrollResearch(-1);
            } else {
                scrollResearch(1);
            }
            e.preventDefault ? e.preventDefault() : (e.returnValue = false);
        }
    }

    var MOVE_CAMERA = true;

    function doMouseUp(e) {
        MOVE_CAMERA = false;
    };
}