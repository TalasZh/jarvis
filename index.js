// var self = require('sdk/self');

// // a dummy function, to show how tests work.
// // to see how to test this function, look at test/test-index.js
// function dummy(text, callback) {
//   callback(text);
// }


exports.main = function (options) {

    var { ToggleButton } = require('sdk/ui/button/toggle');
    var tabs = require("sdk/tabs");
    var simpleStorage = require("sdk/simple-storage");
    var simplePrefs = require("sdk/simple-prefs");
    var pageMod = require("sdk/page-mod");
    var data = require('sdk/self').data;
    var sidebarCtrl = require("sdk/ui/sidebar");
    var JiraApi = require("jira-module").JiraApi;
    var MediatorApi = require("mediator-api").MediatorApi;
    var panels = require("sdk/panel");

    var floatingCtrls = [];
    var currentSessionStatus = {
        isAnnotationReadonly: true,
        isAnnotatorOn: false,
        activeResearch: "null",
        jarvisHost: simplePrefs.prefs.jarvisHost,
        jiraHost: simplePrefs.prefs.jiraHost,
        annotations: []
    };

    var searchQuery = 'issuetype = Research AND status not in (Resolved, Closed, Done) AND resolution = Unresolved AND assignee in (currentUser()) ORDER BY updatedDate DESC';
    var researchWorkers = [];
    var sidebars = [];
    var researches = [];
    var jiraError = null;


    var jira = new JiraApi(simplePrefs.prefs.jiraHost, "2", false, false);
    var mediator = new MediatorApi(simplePrefs.prefs.jarvisHost, null, null, true);

    var __indexOf = [].indexOf || function (item) {
            for (var i = 0, l = this.length; i < l; i++) {
                if (i in this && this[i] === item)return i
            }
            return -1
        };

    simplePrefs.on("applyChanges", onPrefChange);

    pullResearches(false);

    var sidebar = sidebarCtrl.Sidebar({
        id: "annotationsSidebar",
        title: "Annotations",
        width: 550,
        url: data.url("annotation_list/annotation-sidebar.html"),
        onAttach: function (worker) {
            worker.port.on("openAnnotationLink", function (annotation) {
                currentSessionStatus.isAnnotatorOn = true;
                currentSessionStatus.isAnnotationReadonly = true;
                currentSessionStatus.activeResearch = annotation.researchSession;
                tabs.activeTab.url = annotation.uri;
            });
            sidebars.push(worker);
        },
        onShow: function () {
            console.log("Sidebar is showing...");
            loadAnnotationToSidebar();
        }
    });

    function loadAnnotationToSidebar() {
        var researches = {};
        for (var inx = 0; inx < currentSessionStatus.annotations.length; inx++) {
            var annotation = currentSessionStatus.annotations[inx];
            var researchKey = annotation.researchSession;
            if (!researches[researchKey]) {
                researches[researchKey] = {key: researchKey, sessions: []};
            }
            researches[researchKey].sessions.push(annotation);
        }


        for (var worker in sidebars) {
            if (sidebars.hasOwnProperty(worker)) {
                console.log("Worker is not null");
                console.log(worker);
                sidebars[worker].port.emit("loadResearchSessions", researches);
            }
        }
    }

    var panel = panels.Panel({
        contentURL: data.url("session-panel/session-panel.html"),
        contentScriptFile: [
            data.url("jquery-2.1.3.min.js"),
            data.url("session-panel/session-panel.js")
        ],
        onShow: function () {
            panel.port.emit("loadResearches", researches);
        }
    });

    panel.port.on("startResearchSession", function (researchKey) {
        currentSessionStatus.activeResearch = researchKey;
        currentSessionStatus.isAnnotatorOn = true;
        currentSessionStatus.isAnnotationReadonly = false;
        button.icon = {
            "16": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png'),
            "32": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png'),
            "64": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png')
        };
        button.state(
            "tab", {
                checked: true
            }
        );
        onPrefChange();
        tabs.activeTab.reload();
        panel.hide();
    });

    var button = ToggleButton({
        id: "jarvis-activator",
        label: "Enable/Disable Jarvis",
        checked: false,
        icon: {
            "16": data.url('jarvis_logo_16x16_cropped.png'),
            "32": data.url('jarvis_logo_32x32_cropped.png'),
            "64": data.url('jarvis_logo_64x64_cropped.png')
        },
        onChange: function () {
            //this.checked = !this.checked;
            if (currentSessionStatus.activeResearch === "null") {
                console.log(panel);
                //fixme seems like a bug in firefox api
                //this.checked = false;
                //for (let tab in tabs) {
                //    try {
                //        button.state(tab, {
                //            checked: false
                //        });
                //    }
                //    catch (ex) {
                //        console.error(ex.message);
                //    }
                //}

                button.state("window", {
                    checked: false
                });

                panel.show({
                    position: button
                });
                this.icon = {
                    "16": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png'),
                    "32": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png'),
                    "64": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png')
                };
                return;
            }
            else {
                onPrefChange();
                tabs.activeTab.reload();
            }
            console.log("Chaning button state");
            var current = this.state("tab").checked;
            current = !current;
            if (current) {
                this.state("tab", {checked: true});
                button.checked = true;
                currentSessionStatus.isAnnotatorOn = true;
                currentSessionStatus.isAnnotationReadonly = false;
                this.icon = {
                    "16": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png'),
                    "32": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png'),
                    "64": data.url('mfb/ic_visibility_black_36dp/web/ic_visibility_black_36dp_2x.png')
                };
            }
            else {
                this.state("tab", {checked: false});
                button.checked = false;
                currentSessionStatus.isAnnotatorOn = false;
                currentSessionStatus.isAnnotationReadonly = true;
                this.icon = {
                    "16": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png'),
                    "32": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png'),
                    "64": data.url('mfb/ic_visibility_off_black_36dp/web/ic_visibility_off_black_36dp_2x.png')
                };
            }

        }
    });

    var researchCtrl = pageMod.PageMod({
        include: ["*"],
        attachTo: ["top"],
        contentStyleFile: [
            data.url("mfb/custom.css"),
            data.url("mfb/mfb.css"),
            data.url("mfb/index.css"),
            data.url("annotator-full.1.2.10/annotator.min.css")
        ],
        contentScriptWhen: "ready",
        contentScriptFile: [
            data.url("jquery-2.1.3.min.js"),
            data.url("list.min.js"),
            data.url("annotator-full.1.2.10/annotator-full.min.js"),
            data.url("annotator.offline.min.js"),
            data.url("floatingElement.js"),
            data.url("annotator.jarvis.store.js")
        ],
        onAttach: function (worker) {
            console.log("Worker initialized");
            jira.getCurrentUser(function (error, responseText) {
                if (error) {
                    worker.port.emit("onErrorMessage", error);
                }
                else {
                    pullJiraResearches();
                    pullAnnotations(worker);

                }

            });
            worker.port.emit("loadResource", data.load("mfb/fbButtons.html"), "body");

            worker.port.emit("setCurrentSessionStatus", currentSessionStatus);

            worker.port.on("updateCurrentSession", function (updatedSession) {
                console.log("Updating current session");
                console.log(updatedSession);
                currentSessionStatus.activeResearch = updatedSession.activeResearch;
                currentSessionStatus.isAnnotationReadonly = updatedSession.isAnnotationReadonly;
                currentSessionStatus.isAnnotatorOn = updatedSession.isAnnotatorOn;

                if (currentSessionStatus.activeResearch === "null") {
                    mediator.getSession(currentSessionStatus.activeResearch, function (error, json) {
                        if (error) {
                            console.log("Error: " + error);
                            worker.port.emit("onErrorMessage", error);
                        }
                        else if (!json) {
                            console.log("Starting session");
                            mediator.startSession(currentSessionStatus.activeResearch, function (error, json) {
                                if (error) {
                                    console.log("Error nothing to do here, seems dead end( " + error);
                                    worker.port.emit("onErrorMessage", "Error nothing to do here, seems dead end( " + error);
                                }
                                else {
                                }
                            });
                        }
                        else {
                            console.log("Research session already started...");
                        }
                    });
                }
            });

            worker.port.on("getResearchList", function () {
                console.log("Jira error: " + jiraError);
                if (jiraError) {
                    console.log("Couldn't retrieve issues");
                    worker.port.emit("setResearches", jiraError, []);
                    //pullResearches(true);
                }
                else if (researches) {
                    console.log("Researches: " + researches);
                    worker.port.emit("setResearches", null, researches);
                }
            });

            worker.port.on("_onAnnotationCreated", function (annotation) {
                _onAnnotationCreated(annotation, function (error, data) {
                    if (error) {
                        worker.port.emit("onErrorMessage", error);
                    }
                    else {
                        console.log();
                        worker.port.emit("_afterAnnotationUpdate", annotation, data);
                        if (__indexOf.call(currentSessionStatus.annotations, data) < 0) {
                            currentSessionStatus.annotations.push(data);
                            loadAnnotationToSidebar();
                        }

                    }
                })
            });

            worker.port.on("_onAnnotationUpdated", function (annotation) {
                console.log("Updating annotation");
                console.log(annotation);
                var preformatted = Object.assign({}, annotation);
                preformatted.ranges = JSON.stringify(annotation.ranges);
                mediator.updateCapture(annotation.researchSession, preformatted, function (error, json) {
                    if (error) {
                        worker.port.emit("onErrorMessage", error);
                    }
                    else {
                        worker.port.emit("_afterAnnotationUpdate", annotation, annotation);
                        for (var inx in currentSessionStatus.annotations) {
                            if (currentSessionStatus.annotations.hasOwnProperty(inx)) {
                                var annot = currentSessionStatus.annotations[inx];
                                if (annot.id === annotation.id) {
                                    currentSessionStatus.annotations[inx] = annotation;
                                }
                            }
                        }
                        loadAnnotationToSidebar();
                    }
                });
            });

            worker.port.on("_onAnnotationDeleted", function (annotation) {
                console.log("Deleting annotation");
                console.log(annotation);
                mediator.deleteCapture(annotation.researchSession, annotation, function (error, json) {
                    if (error) {
                        worker.port.emit("onErrorMessage", error);
                    }
                    else {
                        currentSessionStatus.annotations.splice(currentSessionStatus.annotations.indexOf(annotation), 1);
                    }
                });
            });

            worker.port.on("detach", function () {
                //worker.port.emit("detachMe");
                console.log("Detaching worker from controller...");
                detachWorker(this);
            });

            worker.port.on("showAnnotations", function () {
                sidebar.show();
            });

            researchWorkers.push(worker);
        }
    });

    function detachWorker(worker) {
        var index = researchWorkers.indexOf(worker);
        if (index != -1) {
            researchWorkers.splice(index, 1);
        }
    }

    function pullResearches(redirect) {
        jira.getCurrentUser(function (error, responseText) {
            if (error) {
                jiraError = error;
                if (redirect) {
                    tabs.open(simplePrefs.prefs.jarvisHost);
                }
            }
            else {
                simpleStorage.storage.annotations = {};
                pullJiraResearches();
            }
        });
    }

    function pullAnnotations(worker) {
        console.log("Pulling annotations");
        mediator.listCaptures(function (error, json) {
            if (error) {
                console.error(error + " while pulling annotations");
                if (worker) {
                    worker.port.emit("onErrorMessage", error + " while pulling annotations");
                }
            }
            else {
                console.log("Annotations pulled");
                var annotationsArray = [];
                for (var inx = 0; inx < json.length; inx++) {
                    var annotation = json[inx];
                    var ranges = JSON.parse(annotation.ranges);
                    annotation.ranges = ranges;
                    annotationsArray.push(annotation);
                }
                currentSessionStatus.annotations = annotationsArray;
                worker.port.emit("onLoadAnnotations", annotationsArray);
            }
        });
    }

    function pullJiraResearches() {
        jira.searchJira(searchQuery, ["summary", "status", "assignee", "issuetype"], function (researchError, json) {
            if (researchError) {
                jiraError = researchError;
                console.error("Request completed with errors: " + researchError);
            }
            else {
                jiraError = null;
                researches = json.issues;
                console.log("Researches: " + researches);
            }
        });

    }

    function _onAnnotationCreated(annotation, callback) {
        console.log("Getting session");

        console.log(annotation);
        mediator.getSession(annotation.researchSession, function (error, json) {
            if (error) {
                console.log("Error: " + error);
                callback(error);
            }
            else if (!json) {
                console.log("Starting session");
                mediator.startSession(annotation.researchSession, function (error, json) {
                    if (error) {
                        console.log("Error nothing to do here, seems dead end(" + error);
                        callback("Error nothing to do here, seems dead end( " + error);
                    }
                    else {
                        /**
                         * fixme Gets recursive calls when issue key is changed but id persists
                         * mediator says that selected issue is null but actually while starting
                         * returns issue key with last key
                         */
                        console.log(json);
                        console.log("Trying to save annotation again");
                        _onAnnotationCreated(annotation, callback);
                    }
                });
            }
            else {
                console.log("Saving annotation");
                console.log(JSON.stringify(annotation));
                var preformatted = Object.assign({}, annotation);
                preformatted.ranges = JSON.stringify(annotation.ranges);
                mediator.saveCapture(annotation.researchSession, preformatted, function (error, json) {
                    if (error) {
                        console.log("Error: " + error);
                        callback(error);
                    }
                    else {
                        console.log(json);
                        callback(null, json);
                    }
                });
            }
        });
    }

    function saveNewAnnotation(annotation, duplicate, callback) {
        //TODO temporal workaround for migration purposes

        var temp = duplicate;
        //delete temp.id;
        temp.localId = annotation.id;
        temp.ranges = JSON.stringify(annotation.ranges);


        console.log("Getting session");
        console.log(temp);
        console.log(annotation);
        console.log(duplicate);
        mediator.getSession(temp.researchSession, function (error, json) {
            if (error) {
                console.log("Error: " + error);
            }
            else if (!json) {
                console.log("Starting session");
                mediator.startSession(temp.researchSession, function (error, json) {
                    if (error) {

                        console.log("Error nothing to do here, seems dead end(");
                    }
                    else {
                        /**
                         * fixme Gets recursive calls when issue key is changed but id persists
                         * mediator says that selected issue is null but actually while starting
                         * returns issue key with last key
                         */
                        console.log(json);
                        console.log("Trying to save annotation again");
                        saveNewAnnotation(annotation, duplicate, callback);
                    }
                });
            }
            else {
                console.log("Saving annotation");
                console.log(JSON.stringify(temp));
                mediator.saveCapture(temp.researchSession, temp, function (error, json) {
                    if (error) {
                        console.log("Error: " + error);
                    }
                    else {
                        console.log(json);
                        callback(json);
                    }
                });
            }
        });
    }

    function onPrefChange(callback) {
        jira = new JiraApi(simplePrefs.prefs.jiraHost, "2", false, false);
        mediator = new MediatorApi(simplePrefs.prefs.jarvisHost, null, null, true);
        currentSessionStatus.jarvisHost = simplePrefs.prefs.jarvisHost;
        currentSessionStatus.jiraHost = simplePrefs.prefs.jiraHost;
    }

};
