// var self = require('sdk/self');

// // a dummy function, to show how tests work.
// // to see how to test this function, look at test/test-index.js
// function dummy(text, callback) {
//   callback(text);
// }

var { ActionButton } = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");
var data = require('sdk/self').data;
var pageMod = require("sdk/page-mod");
var searchQuery = 'resolution = Unresolved AND assignee in (currentUser()) ORDER BY updatedDate DESC';
var JiraApi = require("jira-module").JiraApi;
var simplePrefs = require("sdk/simple-prefs");

var floatingCtrls = [];

exports.main = function (options) {

    var researchWorkers = [];
    var researches = [];
    var jiraError = null;
    var currentSession = {
        isAnnotationReadonly: true,
        isAnnotatorOn: false,
        activeResearch: null
    };

    var jira = new JiraApi(simplePrefs.prefs.jiraHost, "2", false, false);

    simplePrefs.on("applyChanges", onPrefChange);

    pullResearches(false);

    var button = ActionButton({
        id: "jarvis-activator",
        label: "Enable/Disable Jarvis",
        icon: {
            "16": data.url('jarvis_logo_16x16_cropped.png'),
            "32": data.url('jarvis_logo_32x32_cropped.png'),
            "64": data.url('jarvis_logo_64x64_cropped.png')
        },
        onClick: handleClick
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
        contentScriptOptions: {currentSession: currentSession},
        contentScriptFile: [
            data.url("jquery-2.1.3.min.js"),
            data.url("list.min.js"),
            data.url("annotator-full.1.2.10/annotator-full.min.js"),
            data.url("annotator.offline.min.js"),
            data.url("floatingElement.js"),
            data.url("mfb/custom.js")
        ],
        onAttach: function (worker) {

            console.log("Worker initialized");
            worker.postMessage({some: "options"});
            worker.port.on("requestResource", function (resourceName, target) {
                console.log(data.url(resourceName));
                console.log(target);
                worker.port.emit("loadResource", data.load(resourceName), target);
            });

            worker.port.on("getResearchList", function () {
                console.log("Jira error: " + jiraError);
                if (jiraError) {
                    console.log("Couldn't retrieve issues");
                    worker.port.emit("setResearches", jiraError, []);
                    pullResearches(true);
                }
                else if (researches) {
                    console.log("Researches: " + researches);
                    worker.port.emit("setResearches", null, researches);
                }
            });

            worker.port.on("updateCurrentSession", function (updatedSession) {
                console.log("updateCurrentSession" + JSON.stringify(updatedSession));
                currentSession = updatedSession;
            });

            worker.port.on("detach", function () {
                detachWorker(this);
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


    function handleClick(state) {
        tabs.open("https://wiki.ubuntu.com");
    }

    function pullResearches(redirect) {
        isUserAuth(redirect, pullJiraResearches);
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

    function isUserAuth(redirect, callback) {
        jira.getCurrentUser(function (error, responseText) {
            if (error) {
                jiraError = error;
                if (redirect) {
                    tabs.open(simplePrefs.prefs.jiraHost);
                }
            }
            else {
                callback();
            }
        });
    }

    function onPrefChange() {
        jira = new JiraApi(simplePrefs.prefs.jiraHost, "2", false, false);
    }

};
