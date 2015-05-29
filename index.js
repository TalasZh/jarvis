var data = require('sdk/self').data;
var pageMod = require('sdk/page-mod');
var selectors = [];
var panels = require('sdk/panel');
var simpleStorage = require('sdk/simple-storage');
var notifications = require('sdk/notifications');

var tabs = require("sdk/tabs");


var { ToggleButton } = require('sdk/ui/button/toggle');
// var panels = require("sdk/panel");
var self = require("sdk/self");
var {Cc, Ci, Cu} = require("chrome");
var system = require("sdk/system");
var  cm = require("sdk/context-menu");

let { search } = require("sdk/places/history");

const { pathFor } = require('sdk/system');
const path = require('sdk/fs/path');
const file = require('sdk/io/file');
const JiraApi = require('jira-module').JiraApi;
const MediatorApi = require('mediator-api').MediatorApi;

const urls = require("sdk/url");
const clipboard = require("sdk/clipboard");



var jira;
var mediator;
var global_username;


var annotatorIsOn = false;
var matchers = [];

const init = () => {
    if (!mediator) {
        mediator = new MediatorApi('http',
            'jarvis-test.critical-factor.com',
            '8080',
            null,
            null,
            true);
    }
    console.log("hello")
};


if (!simpleStorage.storage.annotations)
    simpleStorage.storage.annotations = {};

simpleStorage.on("OverQuota", function () {
    notifications.notify({
        title: 'Storage space exceeded',
        text: 'Removing recent annotations'
    });
    while (simpleStorage.quotaUsage > 1)
        simpleStorage.storage.annotaions.pop();
});

function Annotation(annotationText, anchor) {
    this.comment = annotationText;
    this.url = anchor[0];
    this.ancestorId = anchor[1];
    this.anchorText = anchor[2];
}

function handleNewAnnotation(annotationText, anchor, sessionKey) {
    var newAnnotation = new Annotation(annotationText, anchor);

    mediator.saveCapture(sessionKey, newAnnotation, function (error, json) {
        if (error) {
            console.error("Error : " + error);
        }
        else {
            console.log(JSON.stringify(json));
            let captureId = json.id;
            simpleStorage.storage.annotations.captureId = json;
        }
    });
    updateMatchers();
}

function onAttachWorker(annotationEditor, data) {
    annotationEditor.annotationAnchor = data;
    annotationEditor.show();
    console.log('On attach worker event...');
}

function detachWorker(worker, workerArray) {
    var index = workerArray.indexOf(worker);
    if (index != -1) {
        workerArray.splice(index, 1);
    }
}

function activateSelectors() {
    selectors.forEach(
        function (selector) {
            selector.postMessage(annotatorIsOn);
        });
}

function toggleActivation() {
    annotatorIsOn = !annotatorIsOn;
    activateSelectors();
    return annotatorIsOn;
}

function updateMatchers() {
    matchers.forEach(function (matcher) {
        matcher.postMessage(simpleStorage.storage.annotations);
    });
}


function getUserIssues(jira, username) {
    jira.getUsersIssues(username, true, function (error, json) {
        if (error != null) {
            // console.log( error );
            console.error("Could not retrieve " + username + "'s issues.");
            return;
        }
        return json;
    });
}


function selectionChanged(event){
    //todo: check for selection.isContiguous
    selectedText = selection.text;
}


exports.main = function () {
    tabs.open("https://wiki.ubuntu.com/");

    var currentIssueKey = "";
    var selector = pageMod.PageMod({
        include: ['*'],
        contentScriptWhen: 'ready',
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
                            data.url('selector.js')],

        onAttach: function (worker) {
            // console.log(jira);
            worker.postMessage(annotatorIsOn);
            selectors.push(worker);
            worker.port.on('show', function (data) {
                onAttachWorker(annotationEditor, data);
            });
            worker.port.on('initAnnotator', function (annotator) {
                console.log(annotator);
            });

            worker.on('detach', function () {
                detachWorker(this, selectors);
            });
        }
    });


    var annotationEditor = panels.Panel({
        width: 220,
        height: 220,
        contentURL: data.url('editor/annotation-editor.html'),
        contentScriptFile: data.url('editor/annotation-editor.js'),
        onMessage: function (annotationText) {
            if (annotationText) {
                console.log(this.annotationAnchor);
                console.log(annotationText);
                console.log(currentIssueKey);
                handleNewAnnotation(annotationText, this.annotationAnchor, currentIssueKey);
            }
            annotationEditor.hide();
        },
        onShow: function () {
            this.postMessage('focus');
        }
    });


    cm.Item({
        label: "Annotate",
        image: self.data.url("icon-16.png"),
        context: [cm.SelectionContext()],
        contentScriptFile: [data.url('login/context-menu.js'),
                            data.url('jquery-2.1.4.js'),
                            data.url('jquery.highlight.js')],
        onMessage: function (data) {
            console.log( "Selected text : "  +  data );
            if ( annotatorIsOn ){
                onAttachWorker(annotationEditor, data);
                annotationEditor.show();
            }
        }
    });


    var annotationList = panels.Panel({
        width: 420,
        height: 200,
        contentURL: data.url('list/annotation-list.html'),
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
                            data.url('list/annotation-list.js')],
        contentScriptWhen: 'ready',
        onShow: function () {
            this.postMessage(simpleStorage.storage.annotations);
        },
        onMessage: function (message) {
            require('sdk/tabs').open(message);
        }
    });


    var matcher = pageMod.PageMod({
        include: ['*'],
        contentScriptWhen: 'ready',
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
                            data.url('matcher.js'),
                            data.url('jquery.highlight.js')],
        onAttach: function (worker) {
            if (simpleStorage.storage.annotations) {
                worker.postMessage(simpleStorage.storage.annotations);
            }
            worker.port.on('show', function (data) {
                annotation.content = data;
                annotation.show();
            });
            worker.port.on('hide', function () {
                annotation.content = null;
                annotation.hide();
            });
            worker.on('detach', function () {
                detachWorker(this, matchers);
            });
            matchers.push(worker);
        }
    });

    var annotation = panels.Panel({
        width: 200,
        height: 180,
        contentURL: data.url('annotation/annotation.html'),
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
                            data.url('annotation/annotation.js')],
        onShow: function () {
            this.postMessage(this.content);
        }
    });

    var showIssue = ToggleButton({
        id: "show-issue",
        label: "Show Issue",
        icon: {
            "16": data.url('icon-16.png'),
            "32": data.url('icon-32.png'),
            "64": data.url('icon-64.png')
        },
        onClick: function (state) {
            if (state.checked) {
                init();
                mediator.getIssue("JAR-2", function (error, json) {
                    if (error !== null) {
                        console.error("Error: " + error);
                    }
                    else if (json !== undefined) {
                        console.log("Response: " + JSON.stringify(json));
                        issueView.port.emit('set-issue', json);
                        currentIssueKey = json.key;
                    }
                });
                issueView.show({
                    position: showIssue
                });
            }
        }
    });

    var issueView = panels.Panel({
        height: 600,
        width: 350,
        contentScriptFile: [data.url('issue-view/issue-view.js'),
            data.url('jquery-2.1.3.min.js')],
        contentScriptWhen: "start",
        contentURL: data.url("issue-view/issue-view.html"),
        onShow: function () {
        },
        onHide: function (state) {
            showIssue.state('window', {checked: false});
        }
    });

    issueView.port.on('select-issue', function (issueKey) {
        issueView.contentURL = data.url("issue-view/issue-view.html");
        issueView.contentScriptFile = [data.url('issue-view/issue-view.js'),
            data.url('jquery-2.1.3.min.js')];
        console.log("SelectedIssueKey: " + issueKey);
        mediator.getIssue(issueKey, function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
            }
            else if (json !== undefined) {
                console.log("Response: " + JSON.stringify(json));
                issueView.port.emit('set-issue', json);
                currentIssueKey = json.key;
            }
        });
    });

    issueView.port.on('get-annotations', function (issueKey) {
        let annotations = [
            {
                id: 1,
                issueId: issueKey,
                url: "http://getbootstrap.com/components/#glyphicons",
                comment: "This is awesome resource",
                ancestorId: "",
                anchorText: "sdfgsewrysdfbsdf sdrtyse"
            }
        ];
        issueView.port.emit('set-annotations', annotations);
    });


    var button = ToggleButton({
        id: "my-button",
        label: "Jarvis",
        icon: {
            "16": data.url('icon-16.png'),
            "32": "./icon-32.png",
            "64": "./icon-64.png"
        },
        onChange: function (state) {
            if (state.checked) {
                panel.show({
                    position: button
                });
            }
        }
    });


    var panel = panels.Panel({
        width: 350,
        height: 500,
        contentURL: data.url("login/panel.html"),
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
            data.url('issue-view/issue-view.js'),
            data.url('login/handleLogin.js')],
        onHide: function (state) {
            button.state('window', {checked: false});
        }
    });

    // When the panel is displayed it generated an event called
    // "show": we will listen for that event and when it happens,
    // send our own "show" event to the panel's script, so the
    // script can prepare the panel for display.
    panel.on("show", function () {
        panel.port.emit("show");
    });


    panel.port.on("stop-progress", function (issueId) {
        console.log("Stop progress.");
        mediator.stopSession(issueId, function(error, json){
            if (error) {
                console.error("Error: " + error);
            }
            else {
                console.log("Session stopped for Research: " + issueId);
                panel.port.emit('set-session', json);
            }
        });
    });


    panel.port.on("start-progress", function (issueId) {
        console.log("Start progress.");
        mediator.startSession(issueId, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else {
                console.log("Session started for Research: " + issueId);
                panel.port.emit('set-session', json);
            }
        });

    });

    panel.port.on('pause-session', function(sessionKey){
        console.log("Pause session.");
        mediator.pauseSession(sessionKey, function(error, json){
            if (error) {
                console.error("Error: " + error);
            }
            else {
                console.log("Session paused for Research: " + sessionKey);
                panel.port.emit("set-session", json);
            }
        });
    });


    panel.port.on('left-click', function () {
        console.log('activate/deactivate');
        toggleActivation();
    });

    panel.port.on('right-click', function () {
        console.log('show annotation list');
        annotationList.show();
    });


    panel.port.on("back-button-pressed", function (projectName) {
        mediator.listProjectIssues(projectName, function (error, json) {
            if (error != null) {
                // console.log( error );
                console.error("Could not retrieve " + global_username + "'s issues.");
                return;
            }
            panel.contentURL = data.url("login/research.html");
            panel.port.emit("fill-combo-box", json);
        });
    });


    panel.port.on("back-button-pressed-on-researchpage", function () {
        mediator.listProjects(function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
                return
            }

            panel.contentURL = data.url("login/selectProject.html");
            panel.port.emit("fill-project-combobox", json);
        });
    });


    panel.port.on("back-button-pressed-on-project-selection-page", function () {
        panel.contentURL = data.url("login/panel.html");
    });


    /**
     * Event triggered when issues was selected from combo box
     */
    panel.port.on("issue-selected", function (selectedIssueKey) {
        mediator.getIssue(selectedIssueKey, function (error, json) {
            if (error !== null) {
                console.error(error + ": Could not retrieve " + "'s issues.");
                return;
            }
            panel.contentURL = data.url("issue-view/issue-view.html");
            panel.port.emit("set-issue", json);
            currentIssueKey = json.key;
        });
    });

    /**
     * Methods for navigating through issues and project
     */
    panel.port.on('select-issue', function (issueKey) {
        console.log("SelectedIssueKey: " + issueKey);

        mediator.getIssue(issueKey, function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
            }
            else if (json !== undefined) {

                console.log("Response: " + JSON.stringify(json));
                panel.port.emit('set-issue', json);
                currentIssueKey = json.key;
            }
        });
    });

    /**
     * Get session full information, returns null if session wasn't started yet
     */
    panel.port.on('get-session', function (sessionKey) {
        mediator.getSession(sessionKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else if (json) {
                console.log(JSON.stringify(json));
                panel.port.emit('set-session', json);
            }
            else {
                console.warn("Session doesn't exist for sessionKey: " + sessionKey);
                panel.port.emit("set-session", null);
            }
        });
    });

    /**
     * Function for retrieving session annotations
     */
    panel.port.on('get-annotations', function (sessionKey) {

        mediator.listSessionCaptures(sessionKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else {
                panel.port.emit('set-annotations', json);
            }
        });
    });


    panel.port.on("link-clicked", function (issueId) {
        tabs.open("http://test-jira.critical-factor.com/browse/" + issueId);
    });


    panel.port.on("project-changed", function (projectKey) {
        mediator.getProject(projectKey, function (error, json) {
            if (error != null) {
                console.error(error);
                console.error("Could not retrieve projects from JIRA.");
                return;
            }
            panel.port.emit("update-project-information", json);
        });
    });


    panel.port.on("project-selected", function (projectName) {
        mediator.listProjectIssues(projectName, function (error, json) {
            if (error != null) {
                // console.log( error );
                console.error("Could not retrieve " + global_username + "'s issues.");
                return;
            }
            panel.contentURL = data.url("login/research.html");
            panel.port.emit("fill-combo-box", json);
        });
    });


    // Listen for messages called "text-entered" coming from
    // the content script. The message payload is the text the user
    // entered.
    // In this implementation we'll just log the text to the console.
    panel.port.on("handle-login", function (username, password) {
        console.log(username + " " + password);

        global_username = username;

        mediator = new MediatorApi('http',
            'jarvis-test.critical-factor.com',
            '8080',
            null,
            null,
            true);

        mediator.listProjects(function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
                return
            }
            panel.contentURL = data.url("login/selectProject.html");
            panel.port.emit("fill-project-combobox", json);
        });
    });
};
