var canvas, engine, scene;

var b;

// Data
const GET_API = '../styles/timeline/dummy-api/KMS-109.json';
const MAX_EVENTS = 12;
const STATUS_OPENED = "OPENED";
const STATUS_CLOSED = "CLOSED";

var DATA = [];
var FILTERED_DATA = [];
var DATA_LEN = 0;


// Camera args
var MOVE_CAMERA = false;

var clickInitXPos = 0;
var clickInitYPos = 0;
var clickXPos = 0;
var clickYPos = 0;
// ---

function initScene() {
    canvas = document.getElementById("renderCanvas");

    if( canvas === undefined || canvas === null ) {
        return false;
    }

    console.log("Canvas found, init engine");

    engine = new BABYLON.Engine(canvas, false);
    scene = new BABYLON.Scene(engine);

    b = new Builder( scene, MAX_EVENTS );

    b.createMaterials();
    b.createBaseScene();

    //var parameters = {
    //    edge_blur: 1.0,
    //    chromatic_aberration: 1.0,
    //    distortion: 1.0,
    //};

    //var lensEffect = new BABYLON.LensRenderingPipeline('lensEffects', parameters, scene, 1.0, b.camera);

    //var lensEffect = new BABYLON.LensRenderingPipeline('lensEffects', parameters, scene, 1.0);
    //
    //scene.postProcessRenderPipelineManager.attachCamerasToRenderPipeline('lensEffects', camera);

    //importMeshes();

    return scene;
}

function mergeEvents( id, data ) {
    for( var i = 0; i < data.length; i++ ) {
        for( var j = 0; j < DATA[id].events.length; j++ ) {
            if ( data[i].events[j].id != DATA[id].events[j].id ) {
                DATA[id].events.push( data[i] );
            }
        }
    }
}

function updateStatuses( id ) {
    var data = DATA[id];

    data.issueStatuses = [];

    for( var i = 0; i < data.events.length; i++ ) {
        if( data.events[i].eventInfo.indexOf( "null ->" ) >= 0 ) {
            var stat = {};
            stat.status = STATUS_OPENED;
            stat.date = data.events[i].eventDate;
            data.issueStatuses.push( stat );
        }

        if( data.events[i].eventInfo.indexOf( "-> Closed" ) >= 0 ) {
            var stat = {};
            stat.status = STATUS_CLOSED;
            stat.date = data.events[i].eventDate;
            data.issueStatuses.push( stat );
        }
    }

    if( data.issueStatuses.length == 0 ) {
        var stat = {};
        stat.status = data.status;
        stat.date = 0;
        data.issueStatuses.push( stat );
    }
    else {
        data.issueStatuses.sort(function(a, b) {
            return new Date(b.date) - new Date(a.date);
        });
    }
}

var addedToMenu = [];
function filterData( ) {
    var value = $('#member-dropdown ').val();
    var filterDataLen = 0;
    var length = 0;
    FILTERED_DATA = [];

    if( value == 0 ) {
        FILTERED_DATA = jQuery.extend({}, DATA);

        filterDataLen = DATA_LEN;

        for( var i = 0; i < DATA.length; i++ ) {
            if( DATA[i] != undefined && DATA[i] != null ) {
                for (var j = 0; j < DATA[i].events.length; j++) {

                    if( addedToMenu.indexOf( DATA[i].events[j].userId ) == -1 ) {
                        $('#member-dropdown').append(
                            $('<option></option>').val(DATA[i].events[j].userId).html(DATA[i].events[j].userName)
                        );

                        addedToMenu.push( DATA[i].events[j].userId );
                    }
                }
                if( i > length ) length = i;
            }
        }
    }
    else {
        for( var i = 0; i < DATA.length; i++ ) {
            if( DATA[i] != undefined && DATA[i] != null ) {

                if( i > length ) length = i;
                for( var j = 0; j < DATA[i].events.length; j++ ) {

                    if( addedToMenu.indexOf( DATA[i].events[j].userId ) == -1 ) {
                        $('#member-dropdown').append(
                            $('<option></option>').val(DATA[i].events[j].userId).html(DATA[i].events[j].userName)
                        );
                        addedToMenu.push( DATA[i].events[j].userId );
                    }

                    if( DATA[i].events[j].userId == value ) {
                        if( !FILTERED_DATA[i] ) {
                            FILTERED_DATA[i] = jQuery.extend({}, DATA[i]);
                            FILTERED_DATA[i].events = [];
                            FILTERED_DATA[i].lane = null;
                            filterDataLen++;
                        }

                        FILTERED_DATA[i].events.push( DATA[i].events[j] );
                    }
                }
            }
        }
    }

    FILTERED_DATA.length = length + 1;

    return filterDataLen;
}

function initGrid( data ) {

    if( data == undefined ) data = [];

    for( var i = 0; i < data.length; i++ ) {
        if( DATA[data[i].id] ) {
            mergeEvents( data[i].id, data[i].events )
        }
        else {
            DATA[data[i].id] = data[i];
            DATA_LEN++;
        }

        updateStatuses( data[i].id );
    }

    var length = filterData( );
    b.updateGrid( length );

    var issuePosition = 0;


    for( var i = 0; i < FILTERED_DATA.length; i++ )
        if( FILTERED_DATA[i] != undefined && FILTERED_DATA[i] != null && !FILTERED_DATA[i].lane ) {
            FILTERED_DATA[i].lane = issuePosition++;
        }


    for( var i = 0; i < FILTERED_DATA.length; i++ )
        if( FILTERED_DATA[i] != undefined && FILTERED_DATA[i] != null ) {
            createIssueLanes( FILTERED_DATA[i] );
            for( var j = 0; j < FILTERED_DATA[i].events.length; j++ ) {
                b.createEvent( FILTERED_DATA[i].events[j].id, i, b.vlanes[FILTERED_DATA[i].lane].position.x, getZCoordinate( FILTERED_DATA[i].events[j].eventDate ) * b.laneInterval, FILTERED_DATA[i].events[j].eventType, FILTERED_DATA[i].type, FILTERED_DATA[i].events[j].eventInfo )
            }
        }
}

function createIssueLanes( data ) {
    var firstEventClosed = null;
    var hasIssues = false;
    for( var j = 0; j < data.issueStatuses.length; j++ ) {
        if( data.issueStatuses[j].status == STATUS_OPENED ) {
            var zPos = 0;
            var length = 0;

            if( firstEventClosed == null ) {
                zPos = 0;
                length = getZCoordinate( data.issueStatuses[j].date ) * b.laneInterval;
            }
            else {
                zPos = firstEventClosed;
                length = getZCoordinate( data.issueStatuses[j].date ) * b.laneInterval - zPos;
                firstEventClosed = null;
            }

            b.createIssue(
                data.id,
                data.issueName,
                b.vlanes[data.lane].position.x,
                zPos,
                length,
                data.type
            );

            hasIssues = true;
        }
        if( data.issueStatuses[j].status == STATUS_CLOSED ) {
            firstEventClosed = getZCoordinate( data.issueStatuses[j].date ) * b.laneInterval;
            hasIssues = true;
        }
    }

    if( !hasIssues && data.status == STATUS_CLOSED ) {
        return;
    }

    if( !hasIssues && data.status == STATUS_OPENED ) {
        firstEventClosed = 0;
    }

    if( firstEventClosed != null ) {
        b.createIssue(
            data.id,
            data.issueName,
            b.vlanes[data.lane].position.x,
            firstEventClosed,
            20 * b.laneInterval - firstEventClosed,
            data.type
        );
    }
}

function getZCoordinate( date ) {
    if( date == 0 ) {
        return MAX_EVENTS;
    }

    var today = new Date();
    var comparable = new Date(date);

    return (today - comparable) / 1000 / 3600 / 24;
}

function getData( f ) {
    $.ajax({
        url: GET_API,
        async: false,
        dataType: 'json',
        success: function (response) {
            f( response );
        }
    });
}

var animationGridMove = 0;
function responseToCameraEvents() {
    if (MOVE_CAMERA) {

        if (b.camera.position.x - (clickXPos - clickInitXPos) / 1000 >= b.LEFT_CAM_LIMIT &&
            b.camera.position.x - (clickXPos - clickInitXPos) / 1000 <= b.RIGHT_CAM_LIMIT) {
            b.camera.position.x -= (clickXPos - clickInitXPos) / 1000;


            if (b.camera.position.x >= b.hLineOffset / 2) {
                for (var i = 0; i < b.dates.length; i++) {
                    b.dates[i].position.x -= (clickXPos - clickInitXPos) / 1000;
                }
            } else {
                for (var i = 0; i < b.dates.length; i++) {
                    b.dates[i].position.x = -b.hLineOffset / 2;
                }
            }
        }

        if (b.camera.position.z + (clickYPos - clickInitYPos) / 1000 >= b.cameraZ) {
            var mvmnt = (clickYPos - clickInitYPos) / 1000;

            b.camera.position.z += mvmnt;
            b.subPlane.position.z += mvmnt;
            animationGridMove += mvmnt;


            for (var i = 0; i < b.vlanes.length; i++) {
                b.vlanes[i].position.z += mvmnt;
                b.lanesName[i].position.z += mvmnt;
            }
        }
    }

    if (val != 0) {
        if (b.zPos < 0 && val < 0) {
            b.zPos = 0;
            val = 0;

            b.camera.position.z = b.cameraZ;
            b.subPlane.position.z = b.subPlane.scaling.y / 2;
            for (var i = 0; i < b.vlanes.length; i++) {
                b.vlanes[i].position.z = 0;
                b.lanesName[i].position.z = 1; // @todo replace by const
            }
            return;
        }

        b.camera.position.z += val;
        b.subPlane.position.z += val;
        for (var i = 0; i < b.vlanes.length; i++) {
            b.vlanes[i].position.z += val;
            b.lanesName[i].position.z += val;
        }

        animationGridMove += val;

        val -= 0.01;

        if (parseInt(val * 100) == 0) val = 0;
    }

    if (animationGridMove >= b.laneInterval) {
        b.hlanes[b.zPos % b.hlanes.length].h.position.z = ( b.hlanes.length + b.zPos ) * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].r.position.z = ( b.hlanes.length + b.zPos ) * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].l.position.z = ( b.hlanes.length + b.zPos ) * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].hr.position.z = ( b.hlanes.length + b.zPos ) * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].hl.position.z = ( b.hlanes.length + b.zPos ) * b.laneInterval;

        b.insertDates(b.zPos + b.hlanes.length);

        b.zPos++;
        animationGridMove -= b.laneInterval;

        if (animationGridMove < 0) animationGridMove = 0;
    }

    if (animationGridMove <= -b.laneInterval) {
        b.hlanes[b.zPos % b.hlanes.length].h.position.z = b.zPos * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].r.position.z = b.zPos * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].l.position.z = b.zPos * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].hr.position.z = b.zPos * b.laneInterval;
        b.hlanes[b.zPos % b.hlanes.length].hl.position.z = b.zPos * b.laneInterval;

        b.zPos--;

        animationGridMove += b.laneInterval;
        if (animationGridMove > 0) animationGridMove = 0;
    }
}

canvas = document.getElementById("renderCanvas");
canvas.addEventListener("mousedown", doMouseDown, false);
canvas.addEventListener("mousemove", doMouseMove, false);
canvas.addEventListener("mouseup", doMouseUp, false);

function doMouseDown(e) {
    $('#popup').hide();
    MOVE_CAMERA = true;
    clickXPos = clickInitXPos = e.offsetX;
    clickYPos = clickInitYPos = e.offsetY;
};

function doMouseMove(e) {
    clickXPos = e.offsetX;
    clickYPos = e.offsetY;
};

function doMouseUp(e) {
    MOVE_CAMERA = false;
};


if (window.addEventListener) {
    window.addEventListener("mousewheel", MouseWheelHandler, false);
}


var time = 0;
var rep = 1;
var moveForward = 0;
var val = 0;
function MouseWheelHandler(e) {

    $('#popup').hide();

    var e = window.event || e; // old IE support
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
    var stop = false;

    if( delta != moveForward ) {
        animationGridMove = 0;
        rep = 1;
        val = 0;
        stop = true;
    }

    if( delta > 0 ) {
        val = +0.1;
        moveForward = 1;
    }

    if( delta < 0 ) {
        val = -0.1;
        moveForward = -1;
    }

    if( val > 1 ) val = 1;

    if( stop ) {
        val = 0;
    }

    if( new Date().getTime() - time >  500 ) {
        time = new Date().getTime();
        rep = 1;
    }
    else {
        rep += 1;
        val *= rep > 15 ? 15 : rep;
    }

    return false;
}

document.addEventListener("DOMContentLoaded", function () {
    if (BABYLON.Engine.isSupported()) {
        initScene();

        getData( initGrid );

        engine.runRenderLoop(function () {
            scene.render();
            responseToCameraEvents();
        });
    }
}, false);


$('#member-dropdown').on("change", function() {
    for( var i = 0; i < FILTERED_DATA.length; i++ )

        if( FILTERED_DATA[i] != undefined && FILTERED_DATA[i] != null ) {
            for( var j = 0; j < FILTERED_DATA[i].events.length; j++ ) {
                if( b.scene.getMeshByID("event" + FILTERED_DATA[i].events[j].id) )
                    b.scene.getMeshByID("event" + FILTERED_DATA[i].events[j].id).dispose();
            }

            while( b.scene.getMeshByID("issue" + FILTERED_DATA[i].id) != null ) {
                b.scene.getMeshByID("issue" + FILTERED_DATA[i].id).dispose();
            }

            b.scene.getMeshByID( "issueName" + i).dispose();
        }

    b.lanesName = [];
    initGrid();
});

//function closedialog() {
    //$( '#minimap').hide();
//}

//$('#minimap').on('mouseover', '.minimap-event', function() {
//    $('.minimap-event').css( 'color', 'white' );
//
//    $(this).css( 'color', 'yellow' );
//
//    var pid = $(this).closest('.minimap-info').attr('pid');
//
//    for( var i = 0; i < DATA[pid].events.length; i++ ) {
//        b.scene.getMeshByID("event"+DATA[pid].events[i].id).material.alpha = 0.5
//    }
//
//    b.scene.getMeshByID("event"+$(this).attr('pid')).material.alpha = 1;
//});

