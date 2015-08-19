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
    var JiraApi = require("jira-module").JiraApi;
    var MediatorApi = require("mediator-api").MediatorApi;

    var floatingCtrls = [];
    var currentSessionStatus = {
        isAnnotationReadonly: true,
        isAnnotatorOn: false,
        activeResearch: [],
        jarvisHost: simplePrefs.prefs.jarvisHost,
        jiraHost: simplePrefs.prefs.jiraHost,
        annotations: []
    };

    var searchQuery = 'issuetype = Research AND status not in (Resolved, Closed, Done) AND resolution = Unresolved AND assignee in (currentUser()) ORDER BY updatedDate DESC';
    var researchWorkers = [];
    var researches = [];
    var jiraError = null;


    var jira = new JiraApi(simplePrefs.prefs.jiraHost, "2", false, false);
    var mediator = new MediatorApi(simplePrefs.prefs.jarvisHost, null, null, true);

    simplePrefs.on("applyChanges", onPrefChange);

    pullResearches(false);

    var button = ToggleButton({
        id: "jarvis-activator",
        label: "Enable/Disable Jarvis",
        icon: {
            "16": data.url('jarvis_logo_16x16_cropped.png'),
            "32": data.url('jarvis_logo_32x32_cropped.png'),
            "64": data.url('jarvis_logo_64x64_cropped.png')
        },
        onChange: handleClick
    });

    function initialize() {

        onPrefChange();

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
                        worker.port.emit("loadResource", data.load("mfb/fbButtons.html"), "body");
                    }

                });
                worker.port.emit("setCurrentSessionStatus", currentSessionStatus);

                worker.port.on("updateCurrentSession", function (updatedSession) {
                    console.log("Updating current session");
                    console.log(updatedSession);
                    currentSessionStatus.activeResearch = updatedSession.activeResearch;
                    currentSessionStatus.isAnnotationReadonly = updatedSession.isAnnotationReadonly;
                    currentSessionStatus.isAnnotatorOn = updatedSession.isAnnotatorOn;
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

                researchWorkers.push(worker);
            }
        });
    }

    function detachWorker(worker) {
        var index = researchWorkers.indexOf(worker);
        if (index != -1) {
            researchWorkers.splice(index, 1);
        }
    }


    function handleClick(state) {
        //tabs.open(simplePrefs.prefs.jarvisHost);
        state.checked = !state.checked;
        if (state.checked) {
            initialize();
            tabs.activeTab.reload();
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
                console.log(json);
                var annotationsArray = [];
                for (var inx = 0; inx < json.length; inx++) {
                    var annotation = json[inx];
                    var ranges = JSON.parse(annotation.ranges);
                    annotation.ranges = ranges;
                    annotationsArray.push(annotation);
                    console.log(annotation);
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
            }
            else if (!json) {
                console.log("Starting session");
                mediator.startSession(annotation.researchSession, function (error, json) {
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
