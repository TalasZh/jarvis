'use strict';

angular.module('jarvis.structure.ctrl', [
])
    .controller('CanvasCtrl', CanvasCtrl);

CanvasCtrl.$inject = ['$rootScope', '$location', '$scope', '$sessionStorage', 'structureSrv'];

function CanvasCtrl($rootScope, $location, $scope, $sessionStorage, structureSrv)
{
    var vm = this;
    $scope.$storage = $sessionStorage;


    // data from api
    vm.projects = [];
    vm.activeNode;
    vm.activeNodeData;

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

    vm.issuesSolved = {
        research : 0,
        task : 0,
        bug : 0
    };

    vm.requirements = {
        opened : 0,
        closed : 0
    };

    vm.borderColor = {
        coverage : "border-green",
        test : "border-green",
        issues : "border-green"
    };


	vm.knobBigOptions = {
		width: "214",
		height: "220",
		displayPrevious: false,
		fgColor: "#ffff00",
		bgColor: "#001937",
		thickness: ".125",
		displayInput: "false",
		readOnly: true
	};

	vm.knobSmallOptions = {
		width: "155",
		displayPrevious: false,
		fgColor: "#ffff00",
		bgColor: "#001937",
		thickness: ".08",
		displayInput: "false",
		readOnly: true
	};

	//functions
	vm.simulateClick = simulateClick;
    vm.toTimeline = toTimeline;

    // arbor variables
    vm.sys = arbor.ParticleSystem(500, 300, 0.2, false, 20);
    vm.sys.parameters({gravity: true});
    vm.sys.renderer = Renderer("#viewport");

    var promise = structureSrv.getProjects().success(function (data) {
        vm.projects = data;
    });

    promise.then(function() {
        recursivePromises( 0, vm.projects.length );
    });

    function recursivePromises( i, length ) {
        if( i < length ) {
            var j = structureSrv.getIssues("project.json").success(function (data)
            {
                console.log( "Inserting", i );
                vm.projects[i] = data;
                vm.projects[i].issueType = "Project";
            });

            j.then( function() {
                recursivePromises( i + 1, length );
            });
        }
        else {
            drawFromSSF();
            initEvents();
        }
    }

	//click on story from list
    function simulateClick(key) {
		click(vm.sys.getNode(key), vm.sys, false);
	}

    function toTimeline() {
        $location.path('/timeline/' + vm.activeNode.data.title);
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
        var foundationNode = vm.sys.addNode('ssf', {weight: 5, type: "Foundation", title: "SSF"});

        for (var i = 0; i < vm.projects.length; i++) {
            if (!vm.projects[i].issueType) {
                vm.projects[i].issueType = "project";
            }

            if (!node || vm.projects[i].key !== node.name) {

                vm.sys.addNode(vm.projects[i].key, {
                    weight: 4,
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


    function calculateRequirements(currentNode)
    {
        var opened = 0;
        var closed = 0;

        for( var i = 0; i < currentNode.issues.length; i++ )
        {
            if( currentNode.issues[i].issueType == "Requirement" ) {
                if( currentNode.issues[i].status == "Open" ) {
                    opened++;
                }
                else {
                    closed++;
                }
            }
        }

        vm.requirements.opened = Math.round( opened * 100 / ( opened + closed ) );
        vm.requirements.closed = Math.round( closed * 100 / ( opened + closed ) );


        delete $scope.$storage.metrics;
        // @todo changed to storage
        //vm.structureSrv.metrics = {};
        $scope.$storage.metrics = {}
        $scope.$storage.metrics.requirements = vm.requirements;
        //vm.structureSrv.metrics.requirements = vm.requirements;
    }


    /**************
     * @todo improve
     * @param node - clicked node
     * @param particleSystem - drawing system
     */
    function click(node, particleSystem, clickFromCanvas) {
        if (node != null) {
			var currentNode = getNodeDataByPath(node.data.path);

            if (node.data.type.toLowerCase() == "requirement" || node.data.type.toLowerCase() == "design" ||
                node.data.type.toLowerCase() == "task" || node.data.type.toLowerCase() == "playbook") {

				var popup = new Popup();
				popup.getIssuePopup(false, currentNode);
                return;
			}

            if( node.data.type.toLowerCase() == "foundation" ) {
                if( $('#viewport').attr("side-bar-toggled") == "true" ) {
                    $('#v1').hide();
                    $('#viewport').attr("side-bar-toggled", "false");
                    $('#viewport').attr('width', parseInt($("#viewport").attr('width')) + 301);
                }

                return;
            }

			if(currentNode.openStatus !== undefined){
				var openStatus = currentNode.openStatus.originalEstimate;
				var inProgressStatus = currentNode.inProgressStatus.remainingRestimate;
				var doneStatus = currentNode.doneStatus.timeSpent;
				var progressPersent = (openStatus + inProgressStatus + doneStatus) / 100;

				vm.openStatus = Math.round( openStatus / progressPersent, -1 );
				vm.inProgressStatus = Math.round( inProgressStatus / progressPersent, -1 );
				vm.doneStatus = Math.round( doneStatus / progressPersent, -1 );

				vm.openStatusHours = (openStatus / 3600).toFixed(1);
				vm.inProgressStatusHours = (inProgressStatus / 3600).toFixed(1);
				vm.doneStatusHours = (doneStatus / 3600).toFixed(1);
			}
			
            if (node.data.type.toLowerCase() == "story") {

				if( clickFromCanvas ) {
					$rootScope.$apply(function () {
						vm.title = currentNode.key;
						vm.descr = currentNode.summary;
                        vm.activeNodeData = currentNode;
                        calculateRequirements( currentNode );




                        $scope.$storage.metrics.openStatus = vm.openStatus;
                        $scope.$storage.metrics.inProgressStatus = vm.inProgressStatus;
                        $scope.$storage.metrics.doneStatus = vm.doneStatus;
                        $scope.$storage.metrics.openStatusHours = vm.openStatusHours;
                        $scope.$storage.metrics.inProgressStatusHours = vm.inProgressStatusHours;
                        $scope.$storage.metrics.doneStatusHours = vm.doneStatusHours;
                        $scope.$storage.metrics.storyPoints = currentNode.storyPoints;
                        // @todo changed to storage
                        //vm.structureSrv.metrics.openStatus = vm.openStatus;
                        //vm.structureSrv.metrics.inProgressStatus = vm.inProgressStatus;
                        //vm.structureSrv.metrics.doneStatus = vm.doneStatus;
                        //vm.structureSrv.metrics.openStatusHours = vm.openStatusHours;
                        //vm.structureSrv.metrics.inProgressStatusHours = vm.inProgressStatusHours;
                        //vm.structureSrv.metrics.doneStatusHours = vm.doneStatusHours;
                        //vm.structureSrv.metrics.storyPoints = currentNode.storyPoints;

						self.value += 1;
					});
				} else {
                    vm.title = node.data.title;
                    vm.descr = node.data.title;
                    vm.activeNodeData = currentNode;
					node.data.selected = true;

                    calculateRequirements(currentNode);
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
                    vm.activeNodeData = currentNode;

					vm.storyList = currentNode.issues;


                    var total = 0;

                    vm.issuesSolved.task = 0;
                    vm.issuesSolved.bug = 0;
                    vm.issuesSolved.research = 0;


                    for (var name in currentNode.totalIssuesSolved) {
                        if( name == "Story" ) {
                            continue;
                        }

                        var value = currentNode.totalIssuesSolved[name];
                        total += value;

                        // @todo hardcoded need types
                        if( name == "Task" || name == "New Feature" || name == "Improvement" ) {
                            vm.issuesSolved.task += value;
                        }

                        if( name == "Bug" ) {
                            vm.issuesSolved.bug += value;
                        }

                        if( name == "Research" ) {
                            vm.issuesSolved.research += value;
                        }
                    }

                    if( total == 0 ) total = 100;

                    vm.issuesSolved.task = Math.round( vm.issuesSolved.task / total * 100, -1 );
                    vm.issuesSolved.bug = Math.round( vm.issuesSolved.bug / total * 100, -1 );
                    vm.issuesSolved.research = Math.round( vm.issuesSolved.research / total * 100, -1 );

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
                    vm.descr = "NO PROJECT DESCRIPTION!!!!!!!!!!!!!!!!!!!!!";

                    $scope.$storage.project = currentNode;
                    // @todo changed to storage
                    //vm.structureSrv.project = currentNode;
                    vm.activeNodeData = currentNode;


                    if( currentNode.projectStats.coveragePercent >= 80 ) {
                        vm.borderColor.coverage = "border-green";
                        vm.knobBigOptions.fgColor = "#00ff6c";

                    }
                    else if( currentNode.projectStats.coveragePercent >= 40 ) {
                        vm.borderColor.coverage = "border-yellow";
                        vm.knobBigOptions.fgColor = "#ffff00";

                    }
                    else {
                        vm.borderColor.coverage = "border-red";
                        vm.knobBigOptions.fgColor = "#c1272d;";

                    }

                    $('.circle-diagram [knob-options="vm.knobBigOptions"]').trigger(
                        'configure',
                        {
                            "fgColor": vm.knobBigOptions.fgColor
                        }
                    );


                    if( currentNode.projectStats.successPercent >= 90 ) {
                        vm.borderColor.test = "border-green";
                        vm.knobSmallOptions.fgColor = "#00ff6c";
                    }
                    else if( currentNode.projectStats.successPercent >= 80 ) {
                        vm.borderColor.test = "border-yellow";
                        vm.knobSmallOptions.fgColor = "#ffff00";
                    }
                    else {
                        vm.borderColor.test = "border-red";
                        vm.knobSmallOptions.fgColor = "#c1272d;";
                    }

                    $('.circle-diagram [knob-options="vm.knobSmallOptions"]').trigger(
                        'configure',
                        {
                            "fgColor": vm.knobSmallOptions.fgColor
                        }
                    );


                    if( 0 < currentNode.projectStats.criticalIssues && currentNode.projectStats.criticalIssues < 4 ) {
                        vm.borderColor.issues = "border-yellow";
                    }
                    else if( currentNode.projectStats.criticalIssues > 3 || currentNode.projectStats.blockerIssues > 0 ) {
                        vm.borderColor.issues = "border-red";
                    }
                    else {
                        vm.borderColor.issues = "border-green";
                    }


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

        particleSystem.addNode(data.key, {weight: 5, type: data.issueType, title: data.key, path: path});

        for (var i = 0; i < data.issues.length; i++) {
            if (data.issues[i].key != node.name)
                particleSystem.addNode(data.issues[i].key, {
                    weight: 4,
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
                weight: 3,
                type: data.issues[i].issueType,
                title: data.issues[i].key,
                path: node.data.path + "," + i
            });
            particleSystem.addEdge(node.name, data.issues[i].key);

            if (recursively)
                levelDown(particleSystem.getNode(data.issues[i].key), particleSystem, recursively, weight);
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
        var w = 10;
        var wDiff = 10;

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

                selectedObjects['research'] = loadSphere('assets/img/img-research-sphere-selected.png');
                sphereObjects['research'] = loadSphere('assets/img/img-research-sphere.png');

                selectedObjects['bug'] = loadSphere('assets/img/img-bug-sphere-selected.png');
                sphereObjects['bug'] = loadSphere('assets/img/img-bug-sphere.png');

                selectedObjects['task'] = loadSphere('assets/img/img-task-sphere-selected.png');
                sphereObjects['task'] = loadSphere('assets/img/img-task-sphere.png');

                selectedObjects['design'] = loadSphere('assets/img/img-design-sphere-selected.png');
                sphereObjects['design'] = loadSphere('assets/img/img-design-sphere.png');

                selectedObjects['playbook'] = loadSphere('assets/img/img-playbook-sphere-selected.png');
                sphereObjects['playbook'] = loadSphere('assets/img/img-playbook-sphere.png');

                selectedObjects['requirement'] = loadSphere('assets/img/img-requirement-sphere-selected.png');
                sphereObjects['requirement'] = loadSphere('assets/img/img-requirement-sphere.png');

                selectedObjects['story'] = loadSphere('assets/img/img-story-sphere-selected.png');
                sphereObjects['story'] = loadSphere('assets/img/img-story-sphere.png');

                selectedObjects['epic'] = loadSphere('assets/img/img-epic-sphere-selected.png');
                sphereObjects['epic'] = loadSphere('assets/img/img-epic-sphere.png');

                selectedObjects['project'] = loadSphere('assets/img/img-project-sphere-selected.png');
                sphereObjects['project'] = loadSphere('assets/img/img-project-sphere.png');

                selectedObjects['foundation'] = loadSphere('assets/img/img-foundation-sphere-selected.png');
                sphereObjects['foundation'] = loadSphere('assets/img/img-foundation-sphere.png');

                sphereObjects['background'] = loadSphere('assets/img/img-bg-1.jpg');
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

            },

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



        // @todo enhance
        setTimeout(function() {
            var circle = $('.diagram:visible:last');

            var index = $('.diagram').index( circle[0] );
            circle.hide();
            var newCircle = $('.diagram')[ (index + 1) % 3 ];

            // @todo doesn't work
            //newCircle.css('right', '-300px');
            //newCircle.show();
            //newCircle.animate({
            //    right: '+=300px'
            //});
        }, 5000 );
    }
}
