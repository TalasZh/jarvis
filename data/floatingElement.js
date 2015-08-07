(function (window, document, undefined) {
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/index.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/mfb.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/custom.css">');

    var currentSession = {
        isAnnotationReadonly: true,
        isAnnotatorOn: false,
        activeResearch: null
    };


    var annotatorTargetElement = "body";

    var options = {valueNames: ["jarvis-issue-key", "jarvis-issue-type"]};
    var researchIssuesList = new List("jarvis-research-list-ctrl", options);

    self.port.emit("requestResource", "mfb/fbButtons.html", "body");

    self.port.on("setCurrentSessionStatus", function (currentSessionStatus) {
        console.log("Received object");
        console.log(currentSessionStatus);
        currentSession = currentSessionStatus;
        updateAnnotatorStatus();
    });

    self.port.on('detach', function () {
        console.log("Detaching withing the script");
        //Need to disable annotator too
        jQuery("#jarvis-menu").remove();
        destroyAnnotator();
    });

    self.port.on("loadResource", function (resource, target) {
        console.log("resource is loaded");
        console.log(target);
        jQuery(target).append(resource);
        var annotatorControl = jQuery("#btn-jarvis-annotation-control");
        annotatorControl.on("click", function () {
            currentSession.isAnnotatorOn = !currentSession.isAnnotatorOn;
            console.log("Annotator status: " + currentSession.isAnnotatorOn);
            updateAnnotatorStatus();
        });

        var researchList = jQuery("#btn-jarvis-list-researches");
        researchList.on("click", function () {
            console.log("research list is clicked");
            self.port.emit("getResearchList");

            var researchIssueList = jQuery("#jarvis-research-list-ctrl");
            var currentState = researchIssueList.attr("data-jarvis-list-state");
            if (currentState === "collapse") {
                jQuery(researchIssueList).attr("data-jarvis-list-state", "expand");
            }
            else {
                jQuery(researchIssueList).attr("data-jarvis-list-state", "collapse");//, "jarvis-list-state",
                                                                                     // "collapse");
            }
            console.log(currentState);
        });

        var researchListCtrl = jQuery("#jarvis-research-list-ctrl");
        researchListCtrl.on("mouseleave", function () {
            setTimeout(function () {
                var researchIssueList = jQuery("#jarvis-research-list-ctrl");
                if (!jQuery("#btn-jarvis-list-researches").is(":hover")) {
                    researchIssueList.attr("data-jarvis-list-state", "collapse");
                }
            }, 1000);

        });

        $("#searchResearchIssues").keyup(function () {
            var criteria = $(this).val();
            if (criteria) {
                researchIssuesList.search(criteria);
            }
            else {
                researchIssuesList.search();
            }
        });

        researchIssuesList = new List("jarvis-research-list-ctrl", options);
        researchIssuesList.sort("jarvis-issue-key", {order: "asc"});

        //if (currentSession.isAnnotatorOn) {
        //    initializeAnnotator();
        //
        //}
        //enableAnnotator();
    });

    /**
     * Loads current user researches and
     * registers click listeners for each research issue
     */
    self.port.on("setResearches", function (error, researches) {
        console.log("Setting research issues...");
        var researchList = jQuery("#jarvis-research-list-ctrl").find("dl.list");
        researchList.empty();
        if (error) {
            //alert("Please make sure that service is working now. ");
            alert("Error: \n" + error);
        }
        else {
            for (let research of researches) {
                let issueObject = issueTemplateGeneration(research);
                researchList.append(issueObject);

                jQuery(issueObject).attr("data-research-key", research.key);
                jQuery(issueObject).click(function () {
                    currentSession.activeResearch = $(this).attr("data-research-key");
                    currentSession.isAnnotationReadonly = false;
                    currentSession.isAnnotatorOn = true;
                    //enableAnnotator();
                    //enableAnnotation();
                    updateAnnotatorStatus();
                    console.log(currentSession.activeResearch);
                });
            }
            if (researches.length == 0) {
                alert("No Research issues are pulled...");
            }
            researchIssuesList = new List("jarvis-research-list-ctrl", options);
            researchIssuesList.sort("jarvis-issue-key", {order: "asc"});
        }
    });

    function issueTemplateGeneration(research) {
        return jQuery.parseHTML("<dt > <a class='jarvis-issue-key'>" +
            research.key +
            "</a>" +
            "<span class='jarvis-issue-type'>" +
            research.fields.issuetype.name +
            "</span>" +
            "</dt>");
    }

    function updateAnnotatorStatus() {
        console.log("updating annotator status");
        console.log(currentSession);
        self.port.emit("updateCurrentSession", currentSession);
        var annotator = getAnnotator();
        if (currentSession.isAnnotatorOn) {
            if (!annotator) {
                annotator = initializeAnnotator();
            }
            if (currentSession.isAnnotationReadonly) {
                annotator._disableDocumentEvents();
            }
            else {
                if (currentSession.activeResearch) {
                    annotator._setupDocumentEvents();
                }
                else {
                    annotator._disableDocumentEvents();
                    currentSession.isAnnotationReadonly = true;
                }
            }
        }
        else if (annotator) {
            console.log("Destroying annotator and nullifying models");
            annotator.destroy();
            currentSession.isAnnotationReadonly = true;
            currentSession.activeResearch = null;
        }
    }

    /**
     * Initializes annotator plugin if there is any
     * or returns existing one
     * @returns {annotator}
     */
    function initializeAnnotator() {
        var annotator = getAnnotator();
        if (annotator === undefined) {
            var content = jQuery(annotatorTargetElement).annotator({
                readOnly: false
            });

            content.annotator('addPlugin', 'Offline', {
                online: function () {
                    jQuery("#status").text("Online");
                },
                offline: function () {
                    jQuery("#status").text("Offline");
                },
                setAnnotationDataBeforeCreation: function (annotation) {
                    console.log("Annotation data being set...");
                    //console.log(this);
                    annotation.researchSession = currentSession.activeResearch;
                    annotation.uri = window.location.href;
                },
                shouldLoadAnnotation: function (annotation) {
                    return true;
                    //return annotation.researchSession === annotator.options.researchSession;
                },
                getCreatedAnnotation: function (annotation) {
                    console.log("Annotation created event is called");
                    console.log(annotation);
                }
            });
        }

        return annotator;
    }


    function getAnnotator() {
        return jQuery(annotatorTargetElement).data("annotator");
    }

    function enableAnnotator() {

        //var annotator = content.data('annotator');

        //jQuery("#clear-storage").click(function () {
        //    if (annotator) {
        //        annotator.plugins.Offline.store.clear();
        //    }
        //});
    }

    function destroyAnnotator() {
        var annotator = getAnnotator();
        annotator.destroy();
    }

    /**
     * Allow user to create annotations, by clicking on pop-up
     * with little window to set comment
     */
    function enableAnnotation() {
        if (currentSession.activeResearch) {
            var annotator = getAnnotator();
            if (annotator) {
                annotator._setupDocumentEvents();
            }
            else {
                annotator = initializeAnnotator();
                annotator.options.researchSession = currentSession.activeResearch;
            }
        }
    }

    /**
     * Disable user to create annotations, by clicking on pop-up
     * with little window to set comment
     */
    function disableAnnotation() {
        var annotator = getAnnotator();
        if (annotator) {
            annotator._disableDocumentEvents();
        }
    }

})(window, document);
