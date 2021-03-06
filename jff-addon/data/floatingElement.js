(function (window, document, undefined) {
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/index.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/mfb.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/custom.css">');

    var currentSession = {
        isAnnotationReadonly: true,
        isAnnotatorOn: false,
        activeResearch: "null",
        jarvisHost: "",
        jiraHost: "",
        annotations: []
    };


    var annotatorTargetElement = "body",
        options = {valueNames: ["jarvis-issue-key", "jarvis-issue-type"]},
        researchIssuesList = new List("jarvis-research-list-ctrl", options),
        __indexOf = [].indexOf || function (item) {
                for (var i = 0, l = this.length; i < l; i++) {
                    if (i in this && this[i] === item)return i
                }
                return -1
            };


    /**
     * Inherit base configuration from main controller
     * Configurations saved within @currentSession object
     */
    self.port.on("setCurrentSessionStatus", function (currentSessionStatus) {
        console.log("Received object");
        console.log(currentSessionStatus);
        currentSession = currentSessionStatus;
        updateAnnotatorStatus();
    });


    self.port.on("_afterAnnotationUpdate", function (oldAnnotation, newAnnotation) {
        //if (__indexOf.call(this.annotations, annotation) < 0) {

        //}
        //else{
        jQuery.extend(oldAnnotation, newAnnotation);
        //}
        console.log("annotation updated");
        console.log(oldAnnotation);
        console.log(oldAnnotation.highlights[0]);
        console.log(JSON.stringify(oldAnnotation));
        //var annotation = __indexOf()
        console.log(jQuery(oldAnnotation.highlights).data("annotation"));
        jQuery(oldAnnotation.highlights).data("annotation", oldAnnotation);
        //location.reload();
        showNotification("Annotation updated.", Annotator.Notification.SUCCESS);

    });

    self.port.on("onLoadAnnotations", function (annotations) {
        console.log("Annotations loaded...");
        console.log(annotations);
        var annotator = getAnnotator();
        if (annotator) {
            annotator.loadAnnotations(annotations);
        }
        currentSession.annotations = annotations;
    });

    /**
     * For the moment use annotators notification functionality,
     * but later one replace with fully customiezd one
     */
    self.port.on("onErrorMessage", function (errorMsg) {
        currentSession.activeResearch = "null";
        showNotification(errorMsg, Annotator.Notification.ERROR)
    });

    /**
     * Clean up some ui controls
     */
    self.port.on('detach', function () {
        console.log("Detaching withing the script");
        //Need to disable annotator too
        //jQuery("#jarvis-menu").remove();

        var elem = window.document.getElementById("jarvis-menu");
        elem.parentNode.removeChild(elem);
        //elem.remove();

        //self.port.emit("detachWorker", );
        destroyAnnotator();
    });

    /**
     * Import floating buttons to page,
     * with list to start research session
     */
    self.port.on("loadResource", function (resource, target) {
        console.log("resource is loaded");
        console.log(target);
        jQuery(target).append(resource);
        var annotatorControl = jQuery("#btn-jarvis-annotation-control");
        annotatorControl.on("click", function () {
            //currentSession.isAnnotatorOn = !currentSession.isAnnotatorOn;
            console.log("Annotator status: " + currentSession.isAnnotatorOn);
            self.port.emit("showAnnotations");
            //updateAnnotatorStatus();
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


    function showNotification(msg, type) {
        var annotator = getAnnotator();
        if (!annotator) {
            currentSession.isAnnotatorOn = true;
            updateAnnotatorStatus();
        }

        Annotator.showNotification(msg, type);
    }

    function issueTemplateGeneration(research) {
        return jQuery.parseHTML("<dt > <a class='jarvis-issue-key'>" +
            research.key +
            "</a>" +
            "<span class='jarvis-issue-type'>" +
            research.fields.issuetype.name +
            "</span>" +
            "</dt>");
    }

    /**
     * Update main session controller to save
     * active instance across all pages
     */
    function updateAnnotatorStatus() {
        console.log("updating annotator status");
        console.log(currentSession);
        self.port.emit("updateCurrentSession", currentSession);
        var annotator = getAnnotator();
        if (currentSession.isAnnotatorOn) {
            if (!annotator) {
                annotator = initializeAnnotator();
            }
            if (annotator) {
                if (currentSession.isAnnotationReadonly) {
                    annotator._disableDocumentEvents();
                }
                else {
                    if (currentSession.activeResearch !== "null") {
                        annotator._setupDocumentEvents();
                    }
                    else {
                        annotator._disableDocumentEvents();
                        currentSession.isAnnotationReadonly = true;
                    }
                }
            }
        }
        else if (annotator) {
            console.log("Destroying annotator and nullifying models");
            annotator.destroy();
            currentSession.isAnnotationReadonly = true;
            currentSession.isAnnotatorOn = false;
            currentSession.activeResearch = null;
            self.port.emit("updateCurrentSession", currentSession);
            location.reload();
        }
    }

    /**
     * Initializes annotator plugin if there is any
     * or returns existing one
     * @returns {annotator}
     */
    function initializeAnnotator() {
        if (currentSession.jiraError) {
            return undefined;
        }
        var annotator = getAnnotator();

        if (annotator === undefined) {
            var content = jQuery(annotatorTargetElement).annotator({
                readOnly: false
            });

            annotator = getAnnotator();
            annotator.addPlugin("JarvisStore", {
                onAnnotationCreated: function (annotation) {
                    console.log("Annotation created event triggered...");
                    self.port.emit("_onAnnotationCreated", annotation);
                    //console.log(jQuery(annotation.highlights).data("annotation"));
                    currentSession.annotations.push(annotation);
                },
                onAnnotationDeleted: function (annotation) {
                    console.log("Annotation deleted...");
                    if (annotation.id) {
                        currentSession.annotations.splice(currentSession.annotations.indexOf(annotation), 1);
                        self.port.emit("_onAnnotationDeleted", annotation);
                    }
                    else {
                        showNotification("Couldn't delete annotation! Please reload page.", Annotator.Notification.ERROR);
                    }
                },
                onAnnotationUpdated: function (annotation) {
                    console.log("Annotation updated...");
                    if (annotation.id) {
                        self.port.emit("_onAnnotationUpdated", annotation);
                    }
                    else {
                        showNotification("Couldn't update annotation! Please reload page.", Annotator.Notification.ERROR);
                    }
                },
                onBeforeAnnotationCreated: function (annotation) {
                    console.log("Before annotation created...");
                    annotation.researchSession = currentSession.activeResearch;
                    annotation.annotator_schema_version = "v1.2";
                    annotation.uri = window.location.href;
                }
            });
            annotator.setupPlugins({}, {
                Auth: false,
                Tags: false,
                Filter: {
                    addAnnotationFilter: false,
                    filters: [
                        {
                            label: Annotator._t("Comment"),
                            property: "text"
                        },
                        {
                            label: Annotator._t("Quote"),
                            property: "quote"
                        }
                    ]
                },
                Store: false,
                AnnotateItPermissions: false
            });

            console.log("Annotator initialized \nLoading annotations");
            console.log(currentSession.annotations);

            Annotator.showNotification("Active Session: " + currentSession.activeResearch, Annotator.Notification.INFO);

            //content.annotator('addPlugin', 'Offline', {
            //    online: function () {
            //        //jQuery("#status").text("Online");
            //    },
            //    offline: function () {
            //        //jQuery("#status").text("Offline");
            //    },
            //    setAnnotationDataBeforeCreation: function (annotation) {
            //        console.log("Annotation data being set...");
            //        //console.log(this);
            //        annotation.researchSession = currentSession.activeResearch;
            //        annotation.annotator_schema_version = "v1.2";
            //        annotation.uri = window.location.href;
            //    },
            //    setAnnotationData: function (annotation) {
            //        //TODO v1.2 - migration merge where local and remote annotations are coexist
            //        // v1.3 - should deal with unsynced data to sync with server, this step should be gradual
            //        console.log("Annotation Data");
            //
            //        annotation.annotator_schema_version = "v1.2";
            //    },
            //    shouldLoadAnnotation: function (annotation) {
            //        return true;
            //        //return annotation.researchSession === annotator.options.researchSession;
            //    },
            //    getCreatedAnnotation: function (annotation) {
            //        console.log("Annotation created event is called");
            //        console.log(annotation);
            //        //console.log(offline);
            //        self.port.emit("saveUpdateAnnotation", annotation, jQuery.extend(true, {}, annotation));
            //    }
            //});
        }

        return annotator;
    }

    /**
     * Query dom element to extract annotator object
     * @returns {Annotator}
     */
    function getAnnotator() {
        return jQuery(annotatorTargetElement).data("annotator");
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
