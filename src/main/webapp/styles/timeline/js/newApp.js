var canvas, engine, scene, camera;

var GET_API = '../styles/timeline/dummy-api/KMS-109.json';

var camSpeed = {x : 0, z : 0 };
var PINCH_RELEASED = true;
var DATA = [];
var FILTERED_DATA = [];

var appX = 0;
var appY = 0;

var objX = -1;
var objY = -1;

var builder;


var addedToMenu = [];
const STATUS_OPENED = "OPENED";
const STATUS_CLOSED = "CLOSED";


document.addEventListener("DOMContentLoaded", function ()
{
    if (BABYLON.Engine.isSupported())
    {
        initScene();

        engine.runRenderLoop(function ()
        {
            scene.render();
            builder.responseToCameraEvents( camSpeed );

            var pickInfo = scene.pick(appX, appY, function (mesh) { return mesh !== null; });
            if (pickInfo.hit) {
                objX = appX;
                objY = appY;

                $('#popup').show();
                $('#popup').css("top", (appY - 110) + "px");
                $('#popup').css("left", (appX - 125) + "px");
                $('#popup').text( pickInfo.pickedMesh.id );
            }
        });

        getData( updateView );
    }
}, false);

function getData( callback ) {
    $.ajax({
        url: GET_API,
        async: false,
        dataType: 'json',
        success: function (data) {
            if( data == undefined ) data = [];

            for( var i = 0; i < data.length; i++ ) {
                var result = $.grep(DATA, function(e){ return e.id == data[i].id; });

                if( result.length > 0 ) {
                    mergeEvents( data[i].id, data[i].events )
                }
                else {
                    DATA.push( data[i] );
                }

                updateStatuses( data[i].id );
            }

            callback( );
        }
    });
}

function mergeEvents( id, data ) {
    for( var i = 0; i < data.length; i++ ) {
        var result = $.grep(DATA, function(e){ return e.id == id; });

        if( result.length > 0 ) {
            result = result[0];

            for( var j = 0; j < result.events.length; j++ ) {
                if ( data[i].events[j].id != result.events[j].id ) {
                    result.events.push( data[i] );
                }
            }
        }
    }
}

function updateStatuses( id ) {
    var data = $.grep(DATA, function(e){ return e.id == id; });;
    data = data[0];


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
            return new Date(a.date) - new Date(b.date);
        });
    }
}

// @todo rewrite
function filterData( ) {
    var value = $('#member-dropdown ').val();
    FILTERED_DATA = [];

    if( value == 0 ) {
        FILTERED_DATA = cloneArray(DATA);

        for( var i = 0; i < DATA.length; i++ ) {

            for (var j = 0; j < DATA[i].events.length; j++) {

                if( addedToMenu.indexOf( DATA[i].events[j].userId ) == -1 ) {
                    $('#member-dropdown').append(
                        $('<option></option>').val(DATA[i].events[j].userId).html(DATA[i].events[j].userName)
                    );

                    addedToMenu.push( DATA[i].events[j].userId );
                }
            }
        }
    }
    else {
        for( var i = 0; i < DATA.length; i++ ) {

            for( var j = 0; j < DATA[i].events.length; j++ ) {

                if( addedToMenu.indexOf( DATA[i].events[j].userId ) == -1 ) {
                    $('#member-dropdown').append(
                        $('<option></option>').val(DATA[i].events[j].userId).html(DATA[i].events[j].userName)
                    );
                    addedToMenu.push( DATA[i].events[j].userId );
                }

                if( DATA[i].events[j].userId == value ) {
                    var result = $.grep(FILTERED_DATA, function(e){ return e.id == DATA[i].id; });

                    if( result.length == 0 ) {
                        var obj = jQuery.extend({}, DATA[i]);
                        obj.events = [];
                        obj.lane = null;
                        FILTERED_DATA.push( obj );

                        result = $.grep(FILTERED_DATA, function(e){ return e.id == DATA[i].id; });
                    }

                    result = result[0];

                    result.events.push( DATA[i].events[j] );
                }
            }
        }
    }
}

function getZCoordinate( date ) {
    if( date == 0 ) {
        return MAX_EVENTS;
    }

    var lastDate = builder.activeDate;
    var comparable = new Date(date);

    return (comparable - lastDate) / 1000 / 3600 / 24;
}

function cloneArray( data ) {
    var newArr = [];

    for( var i = 0; i < data.length; i++ ) {
        newArr.push( data[i] );
    }

    return newArr;
}

function updateView() {


    filterData();
    builder.updateGrid( FILTERED_DATA.length );

    var issuePosition = 0;


    // @todo sorting
    for( var i = FILTERED_DATA.length - 1; i >= 0; i-- ) {
        if( FILTERED_DATA[i].lane == null ) {
            FILTERED_DATA[i].lane = issuePosition++;
        }
    }

    for( var i = 0; i < FILTERED_DATA.length; i++ ) {
        createIssueLanes(FILTERED_DATA[i]);
        for (var j = 0; j < FILTERED_DATA[i].events.length; j++) {
            builder.createEvent(FILTERED_DATA[i].events[j].id, FILTERED_DATA[i].id, builder.vlanes[FILTERED_DATA[i].lane].position.x, getZCoordinate(FILTERED_DATA[i].events[j].eventDate) * builder.laneInterval, FILTERED_DATA[i].events[j].eventType, FILTERED_DATA[i].type, FILTERED_DATA[i].events[j].eventInfo)
        }
    }
}

function createIssueLanes( data ) {
    var firstEventClosed = null;
    var hasIssues = false;
    for( var j = 0; j < data.issueStatuses.length; j++ ) {
        if( data.issueStatuses[j].status == STATUS_OPENED ) {
            firstEventClosed = getZCoordinate( data.issueStatuses[j].date ) * builder.laneInterval;
            hasIssues = true;
        }
        if( data.issueStatuses[j].status == STATUS_CLOSED ) {
            var zPos = 0;
            var length = 0;

            if( firstEventClosed == null ) {
                zPos = 0;
                length = getZCoordinate( data.issueStatuses[j].date ) * builder.laneInterval;
            }
            else {
                zPos = firstEventClosed;
                length = getZCoordinate( data.issueStatuses[j].date ) * builder.laneInterval - zPos;
                firstEventClosed = null;
            }

            builder.createIssue(
                data.id,
                data.issueName,
                builder.vlanes[data.lane].position.x,
                zPos,
                length,
                data.type
            );

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
        builder.createIssue(
            data.id,
            data.issueName,
            builder.vlanes[data.lane].position.x,
            firstEventClosed,
            20 * builder.laneInterval - firstEventClosed,
            data.type
        );
    }
}


function initScene()
{
    canvas = document.getElementById("renderCanvas");

    if( canvas === undefined || canvas === null ) {
        return false;
    }

    engine = new BABYLON.Engine(canvas, true);
    scene = new BABYLON.Scene(engine);

    builder = new Builder(scene, 10);
    console.log("builder inited");
    builder.createMaterials();
    builder.createBaseScene();

    for( var i = 1; i < 10; i++ ) {
        builder.insertDates(i);
    }

    //builder.updateGrid(10);
    //builder.launchParticles();

    //BABYLON.SceneLoader.ImportMesh("", "assets/", "Fin.babylon", scene, function (meshes) {
    //    console.log(meshes);
    //    var m = meshes[2];
    //    m.isVisible = true;
    //
    //    m.position.z = 5;
    //
    //    scene.beginAnimation(m, 0, 100, true, 1);
    //});
}

function modify( frame, finger )
{
    if( Math.abs( finger.direction[0] ) > 0.4 || Math.abs( finger.direction[1] ) > 0.4 )
    {
        return;
    }

    var appWidth = canvas.width;
    var appHeight = canvas.height;

    var iBox = frame.interactionBox;


    var normalizedPoint = iBox.normalizePoint(finger.stabilizedTipPosition, true);

    appX = normalizedPoint[0] * appWidth;
    appY = (1 - normalizedPoint[1]) * appHeight;

    $('#position').css("left", appX);
    $('#position').css("top", appY);
}
var i = 0;
var controller = new Leap.Controller();

var movementSpeed = 0.5;
var deAcceleration = 0.1;

controller.on( 'frame', function(frame)
{
    var hand = frame.hands[0];

    if( hand )
    {
        modify( frame, hand.fingers[1] );


        if (frame.hands[0].pinchStrength > 0.3)
        {
            if( PINCH_RELEASED ) {
                camSpeed.x = 0;
                camSpeed.z = 0;
                PINCH_RELEASED = false;
            }

            if( Math.abs( parseInt( hand.palmVelocity[0] / 10 )) > 0)
            {
                camSpeed.x -= hand.palmVelocity[0] / 1000 * movementSpeed;
            }


            if( Math.abs( parseInt( hand.palmVelocity[2] / 10 )) > 0)
            {
                camSpeed.z += hand.palmVelocity[2] / 1000 * movementSpeed;
            }
        }
        else
        {
            PINCH_RELEASED = true;

            if( Math.abs( camSpeed.x ) - deAcceleration > 0 ) {
                camSpeed.x -= camSpeed.x > 0 ? 1 : -1 * deAcceleration;
            }
            else
            {
                camSpeed.x = 0;
            }

            if( Math.abs( camSpeed.z ) - deAcceleration > 0 ) {
                camSpeed.z -= camSpeed.z > 0 ? 1 : -1 * deAcceleration;
            }
            else
            {
                camSpeed.z = 0;
            }
        }
    }
    else
    {
        PINCH_RELEASED = true;

        if( Math.abs( camSpeed.x ) - deAcceleration > 0 ) {
            camSpeed.x -= camSpeed.x > 0 ? 1 : -1 * deAcceleration;
        }
        else
        {
            camSpeed.x = 0;
        }

        if( Math.abs( camSpeed.z ) - deAcceleration > 0 ) {
            camSpeed.z -= camSpeed.z > 0 ? 1 : -1 * deAcceleration;
        }
        else
        {
            camSpeed.z = 0;
        }
    }
});

controller.connect();
