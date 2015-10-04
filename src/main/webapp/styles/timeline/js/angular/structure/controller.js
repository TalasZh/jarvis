'use strict';

angular.module('jarvis.structure.ctrl', [
    'jarvis.structure.srv'
])
    .controller('CanvasCtrl', CanvasCtrl);

CanvasCtrl.$inject = ['$rootScope', 'structureSrv'];

function CanvasCtrl($rootScope, structureSrv)
{
    var vm = this;

    // data from api
    vm.projects = [];
    vm.activeNode;

    // hud vars
    vm.title;
    vm.descr;
    vm.openStatus;
    vm.inProgressStatus;
    vm.doneStatus;
    vm.openStatusHours;
    vm.inProgressStatusHours;
    vm.doneStatusHours;
    vm.storyList;
    vm.currentNodeDataPath;

	//functions
	vm.simulateClick;

    // arbor variables
    vm.sys = arbor.ParticleSystem(500, 300, 0.2, false, 20);
    vm.sys.parameters({gravity: true});
    vm.sys.renderer = Renderer("#viewport");

    structureSrv.getProjects().success(function (data) {
        vm.projects = data;
        //for( var i = 0; i < vm.projects.length; i++ ) { @todo enhance critical

        structureSrv.getIssues("AS.json").success(function (data2) {
            for (var i = 0; i < vm.projects.length; i++) {
                vm.projects[i].issues = data2.issues;
            }
        });

        drawFromSSF();
        initEvents();
        //}
    });

	//click on story from list
    vm.simulateClick = function(key) {
		click(vm.sys.getNode(key), vm.sys, false);
	}

    /***************
     * canvas related functions
     ***************/


    /**************
     *
     * @param node - not mandatory, passed by levelUp function
     * creates a base structure
     */
    function drawFromSSF(node) {
        var foundationNode = vm.sys.addNode('ssf', {weight: 3, type: "Foundation", title: "SSF"});

        for (var i = 0; i < vm.projects.length; i++) {
            if (!vm.projects[i].issueType) {
                vm.projects[i].issueType = "project";
            }

            if (!node || vm.projects[i].key !== node.name) {

                vm.sys.addNode(vm.projects[i].key, {
                    weight: 2,
                    type: vm.projects[i].issueType,
                    title: vm.projects[i].key,
                    path: i
                });
            }

            vm.sys.addEdge('ssf', vm.projects[i].key);
        }

        if (!node) {

            vm.activeNode = foundationNode;
            $('#v1').hide();
        }
        $('#v2').hide();
        $('#v3').hide();
    }


    /**************
     *
     * @param path - csv format, position of the object inside vm.projects
     * returns the data received from api
     */
    function getNodeDataByPath(path) {
        var path = path.toString().split(",");

        var workingDataNode = vm.projects;
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


    /**************
     *
     * @param node - clicked node
     * @param particleSystem - drawing system
     */
    function click(node, particleSystem, clickFromCanvas) {
        if (node != null) {
            if (node.data.type.toLowerCase() == "requirement" || node.data.type.toLowerCase() == "design" ||
                node.data.type.toLowerCase() == "task" || node.data.type.toLowerCase() == "playbook")

                return;

            if( node.data.type.toLowerCase() == "foundation" ) {
                if( $('#viewport').attr("side-bar-toggled") == "true" ) {
                    $('#v1').hide();
                    $('#viewport').attr("side-bar-toggled", "false");
                    $('#viewport').attr('width', parseInt($("#viewport").attr('width')) + 301);
                }

                return;
            }

			var currentNode = getNodeDataByPath(node.data.path);

			if(currentNode.openStatus.originalEstimate !== undefined){
				var openStatus = currentNode.openStatus.originalEstimate;
				var inProgressStatus = currentNode.inProgressStatus.originalEstimate;
				var doneStatus = currentNode.doneStatus.originalEstimate;
				var progressPersent = (openStatus + inProgressStatus + doneStatus) / 100;

				vm.openStatus = openStatus / progressPersent;
				vm.inProgressStatus = inProgressStatus / progressPersent;
				vm.doneStatus = doneStatus / progressPersent;

				vm.openStatusHours = (openStatus / 3600).toFixed(2);
				vm.inProgressStatusHours = (inProgressStatus / 3600).toFixed(2);
				vm.doneStatusHours = (doneStatus / 3600).toFixed(2);			
			}
			
            if (node.data.type.toLowerCase() == "story") {
				if( clickFromCanvas ) {
					$rootScope.$apply(function () {
						vm.title = node.data.title;
						vm.descr = node.data.title;

						self.value += 1;
					});
				} else {
                    vm.title = node.data.title;
                    vm.descr = node.data.title;
					node.data.selected = true;
				}

                if ($('#v3').css('display') == 'none') {
            		$('#v1').hide();
					$('#v2').hide();
					$('#v3').css('right', '-300px')
                    $('#v3').show();
                    $("#v3").animate({
                        right: '+=300px'
                    });
                }
            }

            if (node.data.type.toLowerCase() == "epic") {
                $rootScope.$apply(function () {
                    vm.title = node.data.title;
                    vm.descr = currentNode.summary;
                    vm.currentNodeDataPath = node.data.path;
					vm.storyList = currentNode.issues;
                    self.value += 1;
                });

                if ($('#v2').css('display') == 'none') {
					$('#v1').hide();
					$('#v3').hide();
                    $('#v2').css('right', '-300px')
                    $('#v2').show();
                    $("#v2").animate({
                        right: '+=300px'
                    });
                }
            }

            if (node.data.type.toLowerCase() == "project") {

                $rootScope.$apply(function () {
                    vm.title = node.data.title;
                    vm.descr = node.data.title;

                    self.value += 1;
                });


                if ($('#v1').css('display') == 'none') {
					$('#v2').hide();
					$('#v3').hide();
                    $('#v1').css('right', '-300px')
                    $('#v1').show();
                    $("#v1").animate({
                        right: '+=300px'
                    });
                }

                if( $('#viewport').attr("side-bar-toggled") == "true" ) {

                }
                else {
                    $('#viewport').attr("side-bar-toggled", "true");
                    $('#viewport').attr('width', parseInt($("#viewport").attr('width')) - 301);
                }
            }


            if (particleSystem.getEdgesFrom(node, particleSystem).length == 0) {
                levelDown(node, particleSystem, false, node.data.weight + 1);
            }
            else if (particleSystem.getEdgesTo(node, particleSystem).length == 0) {
                levelUp(node, particleSystem);
            }

            vm.activeNode = node;
        }
    }

    /**************
     * proceed to one level up
     * @param node
     * @param particleSystem
     */
    function levelUp(node, particleSystem) {
        deleteEdges(node, particleSystem, true);

        node.data.weight -= 1;

        if (node.data.type.toLowerCase() == "project") {
            drawFromSSF(node);
            return;
        }

        var idx = node.data.path.toString().lastIndexOf(',');

        if (idx <= 0) {
            return;
        }

        var path = node.data.path.substring(0, idx);
        var data = getNodeDataByPath(path);

        particleSystem.addNode(data.key, {weight: 3, type: data.issueType, title: data.key, path: path});

        for (var i = 0; i < data.issues.length; i++) {
            if (data.issues[i].key != node.name)
                particleSystem.addNode(data.issues[i].key, {
                    weight: 2,
                    type: data.issues[i].issueType,
                    title: data.issues[i].key,
                    path: path + "," + i
                });

            particleSystem.addEdge(data.key, data.issues[i].key);
        }
    }

    /**************
     * proceed to one level down
     * @param node
     * @param particleSystem
     */
    function levelDown(node, particleSystem, recursively, weight) {
        var data = getNodeDataByPath(node.data.path);
        if (!data || data.length == 0) return;
        if (data.issues.length == 0) return;


        node.data.weight = weight;

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


    /**************
     *
     * @param canvas - id of canvas
     * @returns {{init: Function, redraw: Function, initMouseHandling: Function}}
     * @constructor - creates render engine for 2d canvas
     */

    function loadSphere( path ) {
        var obj = document.createElement('img');
        obj.src = path;
        return obj;
    }

    function Renderer(canvas) {
        var canvas = $(canvas).get(0);
        var ctx = canvas.getContext("2d");
		var particleSystem;
        var w = 25;
        var wDiff = 15;

        var _mouseP;

        var xc, xy;

        window.addEventListener('resize', resizeCanvas, false);

        function resizeCanvas() {
            canvas.width = window.innerWidth - 110 - 301;
            canvas.height = window.innerHeight;
        }

        resizeCanvas();

        var selectedObjects = [];
        var sphereObjects = [];


        var that = {
            init: function (system) {
                particleSystem = system;
                particleSystem.screenSize(canvas.width - 110 - 301, canvas.height);
                particleSystem.screenPadding(50);

                that.initImg();
                that.initMouseHandling();
            },

            initImg: function() {

                selectedObjects['research'] = loadSphere('/styles/timeline/assets/img/img-task-sphere-selected.png');
                sphereObjects['research'] = loadSphere('/styles/timeline/assets/img/img-task-sphere.png');

                selectedObjects['bug'] = loadSphere('/styles/timeline/assets/img/img-task-sphere-selected.png');
                sphereObjects['bug'] = loadSphere('/styles/timeline/assets/img/img-task-sphere.png');

                selectedObjects['task'] = loadSphere('/styles/timeline/assets/img/img-task-sphere-selected.png');
                sphereObjects['task'] = loadSphere('/styles/timeline/assets/img/img-task-sphere.png');

                selectedObjects['design'] = loadSphere('/styles/timeline/assets/img/img-design-sphere_selected.png');
                sphereObjects['design'] = loadSphere('/styles/timeline/assets/img/img-design-sphere.png');

                selectedObjects['playbook'] = loadSphere('/styles/timeline/assets/img/img-playbook-sphere-selected.png');
                sphereObjects['playbook'] = loadSphere('/styles/timeline/assets/img/img-playbook-sphere.png');

                selectedObjects['requirement'] = loadSphere('/styles/timeline/assets/img/img-requirement-sphere-selected.png');
                sphereObjects['requirement'] = loadSphere('/styles/timeline/assets/img/img-requirement-sphere.png');

                selectedObjects['story'] = loadSphere('/styles/timeline/assets/img/img-story-sphere-selected.png');
                sphereObjects['story'] = loadSphere('/styles/timeline/assets/img/img-story-sphere.png');

                selectedObjects['epic'] = loadSphere('/styles/timeline/assets/img/img-epic-sphere-selected.png');
                sphereObjects['epic'] = loadSphere('/styles/timeline/assets/img/img-epic-sphere.png');

                selectedObjects['project'] = loadSphere('/styles/timeline/assets/img/img-project-sphere-selected.png');
                sphereObjects['project'] = loadSphere('/styles/timeline/assets/img/img-project-sphere.png');

                selectedObjects['foundation'] = loadSphere('/styles/timeline/assets/img/img-project-sphere-selected.png');
                sphereObjects['foundation'] = loadSphere('/styles/timeline/assets/img/img-project-sphere.png');

                sphereObjects['background'] = loadSphere('/styles/timeline/assets/img/img-bg-1.jpg');
            },

            redraw: function () {
                ctx.drawImage(sphereObjects['background'], 0, 0, canvas.width, canvas.height);
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
                        ctx.strokeStyle = "#FFF";
                        ctx.lineWidth = 2;

                        ctx.arc(pt.x, pt.y, width, 0, Math.PI * 2, true);
                        ctx.stroke();
                        ctx.closePath();

                        ctx.drawImage(selectedObjects[node.data.type.toLowerCase()], pt.x - width / 2, pt.y - width / 2, width, width);
                    }
                    else {
                        ctx.drawImage(sphereObjects[node.data.type.toLowerCase()], pt.x - width / 2, pt.y - width / 2, width, width);
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
                            click(dragged.node, particleSystem, true);
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

            }

        }
        return that
    };


    function initEvents() {
        $("#viewport").attr('width', parseInt($("#viewport").attr('width')) + 301);

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
            $("#viewport").attr('width', parseInt($("#viewport").attr('width')) + val);
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

            $("#viewport").attr('width', parseInt($("#viewport").attr('width')) + val);
        });
    }
}
