var canvas, engine, scene, camera;

var GET_API = 'dummy-api/timeline.json';
//var GET_API = 'http://jarvis-test.critical-factor.com:8080/services/api/timeline/story/';

var DATA = [];
var FILTERED_DATA = [];

var appX = 0;
var appY = 0;

var objX = -1;
var objY = -1;

var builder;
var popup;
var eventListener;

var addedToMenu = [];
const STATUS_OPENED = "OPENED";
const STATUS_CLOSED = "CLOSED";

var leapCtrl = new Leap.Controller();

document.addEventListener("DOMContentLoaded", function () {
    if (BABYLON.Engine.isSupported()) {
        initScene();

        engine.runRenderLoop(function () {
            scene.render();
            eventListener.update();
        });

        var key = window.location.search.substring(1);

        getData( key, updateView );
    }
}, false);

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
	popup = new Popup(FILTERED_DATA);

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
                console.log(result['toString']);
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

function getHighlightedStringInText(url, searchText, id) {
    var readabilityUrl = 'https://www.readability.com/api/content/v1/parser?url=' + url + '&token=0626cdbea9b15ece0ad7d9ee4af00c7a6fd13b40&callback=?'
    return $.getJSON(readabilityUrl).then(function(data){
        var contentText = data.content.replace(searchText, '<span class="b-highlight-string">'+searchText+'</span>');
        contentText = contentText.replace(/<img[^>]*>/g, "");
        contentText = contentText.replace('id="content"', '');
        var responseContentDiv = $('.js-content-for-' + id);
        responseContentDiv.html(contentText);
        var topScroll = responseContentDiv.find('.b-highlight-string').offset().top - responseContentDiv.offset().top;
        responseContentDiv.scrollTop(topScroll);
    });
}

$(document).on('click', 'button.btn-close', function () {
    popup.Z_INDEX = 10000;
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
            popup.scrollResearch(-1);
        } else {
            popup.scrollResearch(1);
        }
        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
    }
}

var MOVE_CAMERA = true;

function doMouseUp(e) {
    MOVE_CAMERA = false;
};
