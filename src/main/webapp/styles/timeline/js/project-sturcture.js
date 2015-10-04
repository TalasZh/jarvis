var Renderer = function (canvas) {
    var canvas = $(canvas).get(0);
    var ctx = canvas.getContext("2d");
    var particleSystem;
    var w = 25;
    var wDiff = 15;

    var xc, xy;

    window.addEventListener('resize', resizeCanvas, false);

    function resizeCanvas() {
        canvas.width = window.innerWidth - 110 - 302;
        canvas.height = window.innerHeight;
    }

    resizeCanvas();


    var sphereObject = document.createElement('img');
    sphereObject.src = '/style/timeline/assets/img/img-project-sphere-selected.png';

    var selectedObject = document.createElement('img');
    selectedObject.src = '/style/timeline/assets/img/img-project-sphere.png';

    var background = document.createElement('img');
    background.src = '/style/timeline/assets/img/img-bg-1.jpg';


    var that = {
        init: function (system) {
            particleSystem = system;
            particleSystem.screenSize(canvas.width - 110 - 302, canvas.height);
            particleSystem.screenPadding(50);

            that.initMouseHandling()
        },

        redraw: function () {
            ctx.drawImage(background, 0, 0, canvas.width, canvas.height);
            particleSystem.eachEdge(function (edge, pt1, pt2) {

                ctx.strokeStyle = "rgba(255,255,255, .333)";

                ctx.lineWidth = 1;
                ctx.beginPath();
                ctx.moveTo(pt1.x, pt1.y);
                ctx.lineTo(pt2.x, pt2.y);
                ctx.stroke();
            });

            particleSystem.eachNode(function (node, pt) {

                var width = w + wDiff * node.data.weight;
                ctx.shadowColor = (node.data.alone) ? "red" : "white";
                ctx.save();

                ctx.shadowBlur = 0;
                if (node.data.selected) {

                    ctx.beginPath();
                    ctx.strokeStyle = "#FFF"
                    ctx.lineWidth = 2

                    ctx.arc(pt.x, pt.y, width, 0, Math.PI * 2, true);
                    ctx.stroke();
                    ctx.closePath();

                    ctx.drawImage(sphereObject, pt.x - width / 2, pt.y - width / 2, width, width);
                }
                else {
                    ctx.drawImage(selectedObject, pt.x - width / 2, pt.y - width / 2, width, width);
                }

                ctx.restore();
            });
        },

        initMouseHandling: function () {
            var dragged = null;

            // set up a handler object that will initially listen for mousedowns then
            // for moves and mouseups while dragging
            var handler = {
                clicked: function (e) {
                    var pos = $(canvas).offset();
                    _mouseP = arbor.Point(e.pageX - pos.left, e.pageY - pos.top);
                    dragged = particleSystem.nearest(_mouseP);

                    xc = e.pageX - pos.left;
                    xy = e.pageY - pos.top;

                    if (dragged && dragged.node !== null) {
                        // while we're dragging, don't let physics move the node
                        dragged.node.fixed = true;

                        particleSystem.eachNode(function (node, pt) {
                            node.data.selected = false;
                        });

                        dragged.node.data.selected = true;

                    }

                    $(canvas).bind('mousemove', handler.dragged);
                    $(window).bind('mouseup', handler.dropped);

                    return false
                },
                dragged: function (e) {
                    var pos = $(canvas).offset();
                    var s = arbor.Point(e.pageX - pos.left, e.pageY - pos.top)

                    if (dragged && dragged.node !== null) {
                        var p = particleSystem.fromScreen(s);
                        dragged.node.p = p;
                    }

                    return false
                },

                dropped: function (e) {
                    if (Math.abs(_mouseP.x - xc) + Math.abs(_mouseP.y - xy) <= 10) {
                        click(dragged.node, particleSystem);
                    }

                    if (dragged === null || dragged.node === undefined) return;
                    if (dragged.node !== null) dragged.node.fixed = false;
                    dragged.node.tempMass = 1000;
                    dragged = null;
                    $(canvas).unbind('mousemove', handler.dragged);
                    $(window).unbind('mouseup', handler.dropped);
                    _mouseP = null;

                    return false;
                },

                hover: function (e) {
                    var pos = $(canvas).offset();
                    _mouseP = arbor.Point(e.pageX - pos.left, e.pageY - pos.top);
                    dragged = particleSystem.nearest(_mouseP);

                    if (dragged && dragged.node !== null && dragged.distance <= wDiff * dragged.node.data.weight) {
                        $('#projectInfoPopup').text(dragged.node.data.type + " : " + dragged.node.data.title);
                        $('#projectInfoPopup').show();
                        $('#projectInfoPopup').css("top", e.pageY - pos.top - 50);
                        $('#projectInfoPopup').css("left", e.pageX - pos.left + 20);

                    }
                    else {
                        $('#projectInfoPopup').hide();
                    }
                }
            }


            $(canvas).mousedown(handler.clicked);
            $(canvas).mousemove(handler.hover);

        },

    }
    return that
};

var PROJECT_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project.json";
//var STRUCTURE_API = "http://jarvis-test.critical-factor.com:8080/services/api/timeline/project/";
var STRUCTURE_API = "style/timeline/dummy-api/AS.json";
var DATA = [];

function readProject(callback, sys) {
    $.ajax({
        url: PROJECT_API,
        async: false,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            DATA = data;

            for (var i = 0; i < DATA.length; i++) {
                DATA[i].issues = {};
                readStructure(STRUCTURE_API, i);
            }

            callback(sys);
        }
    });
}

function readStructure(api, i) {
    $.ajax({
        url: api,
        async: false,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success: function (data) {
            console.log(data.issues);
            DATA[i].issues = data.issues;
        }
    });
}

function run(sys) {
    sys.addNode('ssf', {weight: 3, type: "", title: "SSF"});

    for (var i = 0; i < DATA.length; i++) {
        if (!DATA[i].issueType) {
            DATA[i].issueType = "project";
        }
        sys.addNode(DATA[i].key, {weight: 2, type: DATA[i].issueType, title: DATA[i].key, path: i});
        sys.addEdge('ssf', DATA[i].key)
    }
}

function getNodeDataByPath(path) {
    var path = path.toString().split(",");
    var workingDataNode = DATA;
    for (var i = 0; i < path.length; i++) {
        if (workingDataNode.issues) {
            workingDataNode = workingDataNode.issues[path[i]];
        }
        else {
            workingDataNode = workingDataNode[path[i]];
        }
    }

    return workingDataNode;
}

function click(node, particleSystem) {
    if (node != null) {
        if (particleSystem.getEdgesFrom(node, particleSystem).length == 0) {
            levelDown(node, particleSystem, false, node.data.weight + 1);
        }
        else if (particleSystem.getEdgesTo(node, particleSystem).length == 0) {
            levelUp(node, particleSystem);
        }
    }
}

function levelUp(node, particleSystem) {
    if (node.data.type.toLowerCase() == "requirement" || node.data.type.toLowerCase() == "design" ||
        node.data.type.toLowerCase() == "task" || node.data.type.toLowerCase() == "playbook")
        return;

    if (node.data.type.toLowerCase() == "story") {
        deleteEdges(node, particleSystem, true);
    }

    node.data.weight -= 1;
}

function levelDown(node, particleSystem, recursively, weight) {

    if (!recursively) {
        if (node.data.type.toLowerCase() == "requirement" || node.data.type.toLowerCase() == "design" ||
            node.data.type.toLowerCase() == "task" || node.data.type.toLowerCase() == "playbook")
            return;
    }

    var data = getNodeDataByPath(node.data.path);
    if (data.length == 0) return;


    node.data.weight = weight;


    //if( path.length <= 1 && $('#v1').css('display') == 'none' ) {
    //  $('#v1').css('right', '-300px')
    //  $('#v1').show();
    //  $("#v1").animate({
    //    right: '+=300px'
    //  });
    //  $('#v2').hide();
    //  $('#v3').hide();
    //}
    //
    //if( path.length == 2 && $('#v2').css('display') == 'none' ) {
    //  $('#v2').css('right', '-300px')
    //  $('#v2').show();
    //  $("#v2").animate({
    //    right: '+=300px'
    //  });
    //  $('#v1').hide();
    //  $('#v3').hide();
    //}
    //
    //if( path.length >= 3 && $('#v3').css('display') == 'none' ) {
    //  $('#v3').css('right', '-300px')
    //  $('#v3').show();
    //  $("#v3").animate({
    //    right: '+=300px'
    //  });
    //  $('#v2').hide();
    //  $('#v1').hide();
    //
    //}

    if (!recursively) {
        deleteEdges(node, particleSystem, false);
    }

    if (node.data.type.toLowerCase() == "story")
        recursively = true;

    if (!data.issues) return;

    for (var i = 0; i < data.issues.length; i++) {

        particleSystem.addNode(data.issues[i].key, {
            weight: 2,
            type: data.issues[i].issueType,
            title: data.issues[i].key,
            path: node.data.path + "," + i
        });
        particleSystem.addEdge(node.name, data.issues[i].key);

        if (recursively)
            levelDown(particleSystem.getNode(data.issues[i].key), particleSystem, recursively, weight - 1);
    }
}

function deleteEdges(node, particleSystem, directionUp) {
    var edges;

    if (directionUp) {
        edges = particleSystem.getEdgesFrom(node);

        for (var i = 0; i < edges.length; i++) {
            deleteEdges(edges[i].target, particleSystem, directionUp);

            particleSystem.pruneNode(edges[i].target);
        }
    }
    else {
        edges = particleSystem.getEdgesTo(node);

        for (var i = 0; i < edges.length; i++) {
            var parent = edges[i].source;

            var subEdges = particleSystem.getEdgesFrom(parent);
            for (var i = 0; i < subEdges.length; i++) {

                if (subEdges[i].target.name == node.name);
                else {
                    //deleteEdges(subEdges[i].target, particleSystem);
                    particleSystem.pruneNode(subEdges[i].target);
                }

            }

            subEdges = particleSystem.getEdgesTo(parent);
            for (var i = 0; i < subEdges.length; i++) {
                //deleteEdges( subEdges[i].source, particleSystem );
                particleSystem.pruneNode(subEdges[i].source);
            }

            particleSystem.pruneNode(parent);
        }
    }
}