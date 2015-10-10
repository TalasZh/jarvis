'use strict';

angular.module('jarvis.structure.ctrl', [])
    .controller('CanvasCtrl', CanvasCtrl);

CanvasCtrl.$inject = ['$rootScope', 'structureSrv'];

function CanvasCtrl($rootScope, structureSrv) {
    var vm = this;
    var nodes = new vis.DataSet([]);
    var edges = new vis.DataSet([]);

    var selectedNode = null;

    var drawingData = {
        nodes: nodes,
        edges: edges
    };

    var options = {
        autoResize: true,
        height: '100%',
        width: '100%',
        locale: 'en',
        clickToUse: false,
        nodes: {
            size: 5,
            shape: 'image',
            font: {
                face: 'Lato',
                size: 14,
                color: '#ffffff'
            },

        },
        edges: {
            color: {
                color: '#fff',
                opacity: 0.5
            },
            hoverWidth: function (width) {
                return width + 1;
            },
            selectionWidth: function (width) {
                return width;
            },
        },
        physics: {
            enabled: true,
            repulsion: {
                nodeDistance: 200,
                centralGravity: 0.01,
                springConstant: 0.5,
                springLength: 200,
                damping: 0.9,
            },
            maxVelocity: 50,
            minVelocity: 0.3,
            solver: 'repulsion',
            timestep: 0.5,
            stabilization: {
                enabled: true,
                iterations: 1000,
                updateInterval: 25,
                onlyDynamicEdges: false,
                fit: true
            }
        },
        interaction: {hover: true}
    };

    var container = document.getElementById('structureView');
    var network;

    vm.showProgress = true;
    vm.showDevelopers = false;
    vm.showTitles = false;
    vm.showStoryPoints = false;


    var sizes = {};
    sizes["foundation"] = 50;
    sizes["project"] = 40;
    sizes["epic"] = 37;
    sizes["story"] = 33;
    sizes["requirement"] = 30;
    sizes["design"] = 30;
    sizes["playbook"] = 30;
    sizes["task"] = 25;
    sizes["bug"] = 25;
    sizes["improvement"] = 25;
    sizes["feature"] = 25;
    sizes["research"] = 40;

    var color = {};
    color["foundation"] = '#fff';
    color["project"] = '#fff';
    color["epic"] = '#fff';
    color["story"] = '#fff';
    color["requirement"] = '#fff';
    color["design"] = '#fff';
    color["playbook"] = '#fff';
    color["task"] = '#3399FF';
    color["bug"] = '#CC0033';
    color["improvement"] = '#3399FF';
    color["feature"] = '#3399FF';
    color["research"] = '#009933';

    // popup
    var popup = new Popup();
    var timelineData = [];
    var currentStory;

    vm.baseUrl = 'subutai.io';
    vm.jiraUrl = "https://jira.";

    // data from api
    vm.projects = [];
    vm.activeNodeData;

    //breadcrumbs
    vm.breadcrumbsProject = false;
    vm.breadcrumbsEpic = false;

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
    vm.reserchIssueId = 0;

    vm.issuesSolved = {
        research: 0,
        task: 0,
        bug: 0
    };

    vm.requirements = {
        opened: 0,
        closed: 0
    };

    vm.borderColor = {
        coverage: "border-green",
        test: "border-green",
        issues: "border-green"
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
    vm.showReserchPopups = showReserchPopups;
    vm.breadcrumbsClick = breadcrumbsClick;


    var promise = structureSrv.getProjects().success(function (data) {
        vm.projects = data;
    });

    promise.then(function () {
        recursivePromises(0, vm.projects.length);
    });

    function recursivePromises(i, length) {
        if (i < length) {
            var j = structureSrv.getIssues(vm.projects[i].key).success(function (data) {
                vm.projects[i] = data;
                vm.projects[i].issueType = "Project";
            });

            j.then(function () {
                recursivePromises(i + 1, length);
            });
        }
        else {
            //recursion exit

            drawFromSSF();
            initEvents();
        }
    }

    function showReserchPopups(issueId) {
        popup.showMinimap(issueId);
    }


    function breadcrumbsClick(path, element) {
        nodes.remove(nodes.getIds());
        edges.remove(edges.getIds());

        if (path == 'start') {
            vm.breadcrumbsEpic = false;
            vm.breadcrumbsProject = false;
            drawFromSSF();
        } else {
            if (element == 'project') {
                vm.breadcrumbsEpic = false;
            }
            drawFrom(path);
        }

        $('#v1').hide();
        $('#v2').hide();
        $('#v3').hide();
    }

    /***************
     * canvas related functions
     ***************/


    /**************
     *
     * @param node - not mandatory, passed by levelUp function
     * creates a base structure
     */
    var firstInit = true;

    function drawFromSSF() {
        nodes.add({
            id: 'ssf',
            label: 'SSF Foundation',
            image: "assets/img/img-foundation-sphere.png",
            size: sizes["foundation"],
            data: {type: "foundation"}
        });

        for (var i = 0; i < vm.projects.length; i++) {
            nodes.add({
                id: vm.projects[i].key,
                image: "assets/img/img-" + vm.projects[i].issueType.toLowerCase() + "-sphere.png",
                label: vm.projects[i].key,
                size: sizes[vm.projects[i].issueType.toLowerCase()],
                data: {type: vm.projects[i].issueType, path: i}
            });

            edges.add({from: 'ssf', to: vm.projects[i].key});
        }

        if (firstInit) {
            network = new vis.Network(container, drawingData, options);
            initCanvasEvents();
            firstInit = false;
        }
    }

    var modifier = 0;

    function drawFrom(path, node, recursively) {
        var data = getNodeDataByPath(path);

        if (node == undefined || node == null) {
            nodes.add({
                id: data.key,
                label: data.key,
                size: sizes[data.issueType.toLowerCase()],
                image: "assets/img/img-" + data.issueType.toLowerCase() + "-sphere.png",
                data: {type: data.issueType, path: path}
            });
        }
        else if (node.data.type == "Story") {
            recursively = true;
            modifier = 0;
        }


        for (var i = 0; i < data.issues.length; i++) {
            var newPath = path + "," + i;

            nodes.add({
                id: data.issues[i].key,
                label: data.issues[i].key,
                size: sizes[data.issues[i].issueType.toLowerCase()],
                image: "assets/img/img-" + data.issues[i].issueType.toLowerCase() + "-sphere.png",
                data: {type: data.issues[i].issueType, path: newPath}
            });


            if (recursively) {
                if (node.data.type == "Requirement" || node.data.type == "Story")
                    edges.add({
                        from: data.key,
                        to: data.issues[i].key,
                        color: {color: color[data.issues[i].issueType.toLowerCase()]}
                    });
                else
                    edges.add({
                        from: data.key,
                        to: data.issues[i].key,
                        color: {color: color[data.issues[i].issueType.toLowerCase()]},
                        length: 150 * ( Math.floor(modifier++ / 12) + 1 )
                    });

                drawFrom(newPath, nodes.get(data.issues[i].key), true);
            }
            else {
                edges.add({
                    from: data.key,
                    to: data.issues[i].key,
                    color: {color: color[data.issues[i].issueType.toLowerCase()]}
                });
            }
        }

        network.physics.stabilize(110);
    }


    /**************
     *
     * @param path - csv format, position of the object inside vm.projects
     * returns the data received from api
     */
    function getNodeDataByPath(path) {
        if (path !== undefined) {
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
    }

    //getStoryData("AS");
    function getStoryData(key) {
        if (currentStory != key) {
            currentStory = key;
            timelineData = [];
            structureSrv.getEvents(key).success(function (data) {
                var issues = data.issues;

                if (issues == undefined) return;

                for (var i = 0; i < issues.length; i++) {
                    timelineData.push(issues[i]);
                }

                for (var i = 0; i < timelineData.length; i++) {
                    timelineData[i].changelogList.sort(function (a, b) {
                        return b.changeKey.created - a.changeKey.created
                    });
                    timelineData[i].annotations.sort(function (a, b) {
                        return new Date(a.created) - new Date(b.created)
                    });
                    timelineData[i].issueWorkLogs.sort(function (a, b) {
                        return b.createDate - a.createDate
                    });
                }

                popup.setData(timelineData);
            });
        }
    }

    function calculateRequirements(currentNode) {
        var opened = 0;
        var closed = 0;

        for (var i = 0; i < currentNode.issues.length; i++) {
            if (currentNode.issues[i].issueType == "Requirement") {
                if (currentNode.issues[i].status == "Open") {
                    opened++;
                }
                else {
                    closed++;
                }
            }
        }

        vm.requirements.opened = {"percent": Math.round(opened * 100 / ( opened + closed )), "count": opened};
        vm.requirements.closed = {"percent": Math.round(closed * 100 / ( opened + closed )), "count": closed};
    }


    /**************
     * @todo improve
     * @param node - clicked node
     * @param clickFromCanvas - drawing system
     */
    function click(node, clickFromCanvas) {
        if (node == undefined || node == null) return;

        var currentNode = getNodeDataByPath(node.data.path);
        vm.activeNodeData = currentNode;

        if (node.data.type.toLowerCase() == "foundation") {
            $('#v1').hide();
            return;
        }

        if (node.data.type.toLowerCase() == "requirement" || node.data.type.toLowerCase() == "design" ||
            node.data.type.toLowerCase() == "task" || node.data.type.toLowerCase() == "playbook" ||
            node.data.type.toLowerCase() == "research") {

            $rootScope.$apply(function () {
                if (node.data.type.toLowerCase() == "research") {
                    vm.reserchIssueId = currentNode.id;
                }
                else {
                    vm.reserchIssueId = 0;
                }
            });

            popup.getIssuePopup(currentNode.id);
            return;
        }

        if (currentNode.openStatus !== undefined) {
            var openStatus = currentNode.openStatus.originalEstimate;
            var inProgressStatus = currentNode.inProgressStatus.remainingRestimate;
            var doneStatus = currentNode.doneStatus.timeSpent;
            var progressPersent = (openStatus + inProgressStatus + doneStatus) / 100;

            vm.openStatus = Math.round(openStatus / progressPersent, -1);
            vm.inProgressStatus = Math.round(inProgressStatus / progressPersent, -1);
            vm.doneStatus = Math.round(doneStatus / progressPersent, -1);

            vm.openStatusHours = (openStatus / 3600).toFixed(1);
            vm.inProgressStatusHours = (inProgressStatus / 3600).toFixed(1);
            vm.doneStatusHours = (doneStatus / 3600).toFixed(1);
        }

        if (node.data.type.toLowerCase() == "story") {

            vm.title = currentNode.key;
            vm.descr = currentNode.summary;
            calculateRequirements(currentNode);

            if (clickFromCanvas) {
                $rootScope.$apply(function () {
                    self.value += 1;
                });
            } else {
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
            vm.title = node.data.title;
            vm.descr = currentNode.summary;

            vm.storyList = currentNode.issues;


            var total = 0;

            vm.issuesSolved.task = 0;
            vm.issuesSolved.bug = 0;
            vm.issuesSolved.research = 0;


            for (var name in currentNode.totalIssuesSolved) {
                if (name == "Story") {
                    continue;
                }

                var value = currentNode.totalIssuesSolved[name];
                total += value;

                // @todo hardcoded need types
                if (name == "Task" || name == "New Feature" || name == "Improvement") {
                    vm.issuesSolved.task += value;
                }

                if (name == "Bug") {
                    vm.issuesSolved.bug += value;
                }

                if (name == "Research") {
                    vm.issuesSolved.research += value;
                }
            }

            if (total == 0) total = 100;

            vm.issuesSolved.task = Math.round(vm.issuesSolved.task / total, -1);
            vm.issuesSolved.bug = Math.round(vm.issuesSolved.bug / total, -1);
            vm.issuesSolved.research = Math.round(vm.issuesSolved.research / total, -1);

            if (clickFromCanvas) {
                $rootScope.$apply(function () {
                    self.value += 1;
                });
            }

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
            vm.title = node.data.title;
            vm.descr = "NO PROJECT DESCRIPTION!!!!!!!!!!!!!!!!!!!!!";

            if (currentNode.projectStats.coveragePercent >= 80) {
                vm.borderColor.coverage = "border-green";
                vm.knobBigOptions.fgColor = "#00ff6c";

            }
            else if (currentNode.projectStats.coveragePercent >= 40) {
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


            if (currentNode.projectStats.successPercent >= 90) {
                vm.borderColor.test = "border-green";
                vm.knobSmallOptions.fgColor = "#00ff6c";
            }
            else if (currentNode.projectStats.successPercent >= 80) {
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


            if (0 < currentNode.projectStats.criticalIssues && currentNode.projectStats.criticalIssues < 4) {
                vm.borderColor.issues = "border-yellow";
            }
            else if (currentNode.projectStats.criticalIssues > 3 || currentNode.projectStats.blockerIssues > 0) {
                vm.borderColor.issues = "border-red";
            }
            else {
                vm.borderColor.issues = "border-green";
            }

            if (clickFromCanvas) {
                $rootScope.$apply(function () {
                    self.value += 1;
                });
            }

            if ($('#v1').css('display') == 'none') {
                $('#v2').hide();
                $('#v3').hide();
                $('#v1').css('right', '-300px')
                $('#v1').show();
                $("#v1").animate({
                    right: '+=300px'
                });
            }
        }
    }

    function initCanvasEvents() {
        var hovered = false;
        var superX = 0;
        var superY = 0;

        $('#structureView canvas').mousemove(function (e) {
            var x = e.offsetX == undefined ? e.layerX : e.offsetX;
            var y = e.offsetY == undefined ? e.layerY : e.offsetY;

            if (!hovered) {
                superX = x;
                superY = y;
            }
            else if (Math.abs(superX - x) > 70 || Math.abs(superY - y) > 70) {
                $('#projectInfoPopup').css('display', 'none');
                hovered = false;
            }


            $('#projectInfoPopup').css('left', (x + 15) + "px");
            $('#projectInfoPopup').css('top', (y - 30) + "px");
        });

        network.on("hoverNode", function (e) {
            var node = nodes.get(e.node);

            if (node.data.type == "foundation") {
                $('#projectInfoPopup').text("SSF Foundation");
            }

            $('#projectInfoPopup').text(node.data.type.toUpperCase() + " " + node.id.toUpperCase());

            if (node.data.type == "project" || node.data.type == "epic") {

            }

            hovered = true;
            $('#projectInfoPopup').css('display', 'block');
            //var selectedNodeData = nodes.get(selectedNode);
            //
            //console.log(selectedNodeData);
        });

        network.on("click", function (e) {
            selectedNode = e.nodes[0];

            if (selectedNode == null) return;

            var selectedNodeData = nodes.get(selectedNode);
            click(selectedNodeData, true);
        });

        network.on("doubleClick", function (e) {
            selectedNode = e.nodes[0];
            if (selectedNode == null) return;

            var ids = nodes.getIds();

            for (var i = 0; i < ids.length; i++) {
                if (selectedNode != ids[i]) {
                    nodes.remove(ids[i]);
                }
            }

            edges.clear();

            var selectedNodeData = nodes.get(selectedNode);

            var skipDrawing = false;
            $rootScope.$apply(function () {
                if (selectedNodeData.data.type == "Project") {
                    vm.breadcrumbsProject = {path: selectedNodeData.data.path, title: selectedNodeData.label};
                    vm.breadcrumbsEpic = false;
                }
                else if (selectedNodeData.data.type == "Epic") {
                    vm.breadcrumbsEpic = {path: selectedNodeData.data.path, title: selectedNodeData.label};
                }
                else if (selectedNodeData.data.type == "Story") {
                    getStoryData(selectedNodeData.id);
                }
                else {
                    skipDrawing = true;
                }
            });

            if (!skipDrawing)
                drawFrom(selectedNodeData.data.path, selectedNodeData);
        });

        network.on("beforeDrawing", function (ctx) {
            var objIds = nodes.getIds();

            var nodePosition = network.getPositions(objIds);


            if (nodePosition == undefined || nodePosition == null)
                return;

            for (var i = 0; i < objIds.length; i++) {
                drawProgress(ctx, nodePosition[objIds[i]], nodes.get(objIds[i]).size + 5, nodes.get(objIds[i]));
                printDevelopers(ctx, nodePosition[objIds[i]], nodes.get(objIds[i]).size + 5, nodes.get(objIds[i]));
            }


            nodePosition = network.getPositions(selectedNode)[selectedNode];

            if (nodePosition == undefined || nodePosition == null)
                return;

            highlightNode(ctx, nodePosition, nodes.get(selectedNode).size + 20);
        });

        function highlightNode(ctx, nodePosition, size) {
            ctx.save();
            ctx.shadowColor = "#FFF";
            ctx.shadowBlur = 5;

            ctx.globalAlpha = 0.8;
            ctx.strokeStyle = "#FFF";
            ctx.lineWidth = 1;

            ctx.beginPath();
            ctx.arc(nodePosition.x, nodePosition.y, size, -(Math.PI / 4 - Math.PI / 8), -(Math.PI / 4 + Math.PI / 8), true);
            ctx.stroke();

            ctx.beginPath();
            ctx.arc(nodePosition.x, nodePosition.y, size, -Math.PI / 2 - (Math.PI / 4 - Math.PI / 8), -Math.PI / 2 - (Math.PI / 4 + Math.PI / 8), true);
            ctx.stroke();

            ctx.beginPath();
            ctx.arc(nodePosition.x, nodePosition.y, size, -Math.PI - (Math.PI / 4 - Math.PI / 8), -Math.PI - (Math.PI / 4 + Math.PI / 8), true);
            ctx.stroke();

            ctx.beginPath();
            ctx.arc(nodePosition.x, nodePosition.y, size, -Math.PI * 3 / 2 - (Math.PI / 4 - Math.PI / 8), -Math.PI * 3 / 2 - (Math.PI / 4 + Math.PI / 8), true);
            ctx.stroke();
            ctx.closePath();

            ctx.restore();
        }

        function drawProgress(ctx, nodePosition, size, obj) {

            if (!vm.showProgress) return;
            var currentNode;
            var openStatus;
            var inProgressStatus;
            var doneStatus;
            var progressPersent;
            if (obj.data.type == "Project" || obj.data.type == "Epic" || obj.data.type == "Story") {
                currentNode = getNodeDataByPath(obj.data.path);
                openStatus = currentNode.openStatus.originalEstimate;
                inProgressStatus = currentNode.inProgressStatus.remainingRestimate;
                doneStatus = currentNode.doneStatus.timeSpent;
                progressPersent = (openStatus + inProgressStatus + doneStatus) / 100;

                openStatus = Math.round(openStatus / progressPersent, -1) / 100;
                inProgressStatus = Math.round(inProgressStatus / progressPersent, -1) / 100;
                //doneStatus = Math.round( doneStatus / progressPersent, -1 );
            }
            else return;

            console.log(openStatus)
            console.log(inProgressStatus)

            ctx.save();

            ctx.lineWidth = 3;

            ctx.beginPath();
            ctx.strokeStyle = "#29abe2";
            ctx.arc(nodePosition.x, nodePosition.y, size, 0, -Math.PI * 2 * openStatus, true);
            ctx.stroke();
            ctx.closePath();

            ctx.beginPath();
            ctx.strokeStyle = "#fcee21";
            ctx.arc(nodePosition.x, nodePosition.y, size, -Math.PI * 2 * openStatus, -Math.PI * 2 * ( openStatus + inProgressStatus ), true);
            ctx.stroke();
            ctx.closePath();

            ctx.beginPath();
            ctx.strokeStyle = "#00ff6c";
            ctx.arc(nodePosition.x, nodePosition.y, size, -Math.PI * 2 * (openStatus + inProgressStatus), -Math.PI * 2, true);
            ctx.stroke();
            ctx.closePath();

            ctx.restore();
        }

        function drawDevelopers() {

        }

        function drawStoryPoints() {

        }

        function drawBlockers() {

        }

        function printStoryPoints(ctx, nodePosition, size) {
            if (!vm.showStoryPoints) return;

            ctx.save();
            ctx.beginPath();

            ctx.font = "12px Lato";
            ctx.fillStyle = "#FFF";
            ctx.fillText("Devs 24", nodePosition.x - size - 10, nodePosition.y - size - 10);

            ctx.closePath();
            ctx.restore();
        }

        function printDevelopers(ctx, nodePosition, size) {
            if (!vm.showDevelopers) return;

            ctx.save();
            ctx.beginPath();

            ctx.font = "12px Lato";
            ctx.fillStyle = "#FFF";
            ctx.fillText("Devs 24", nodePosition.x - size - 10, nodePosition.y - size - 10);

            ctx.closePath();
            ctx.restore();
        }
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
        });


        // @todo enhance
        $('.js-fade').slick({
            dots: true,
            speed: 300,
            autoplaySpeed: 5000,
            slidesToShow: 1,
            pauseOnHover: true,
            arrows: false,
            autoplay: true,
            fade: true,
            adaptiveHeight: true
        });

        //$('[data-toggle="tooltip"]').tooltip()
    }
}
