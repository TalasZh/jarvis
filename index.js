var data = require('sdk/self').data;
var pageMod = require('sdk/page-mod');
var selectors = [];
var panels = require('sdk/panel');
var simpleStorage = require('sdk/simple-storage');
var notifications = require('sdk/notifications');

var simplePrefs = require("sdk/simple-prefs");

var tabs = require("sdk/tabs");

var { ToggleButton } = require('sdk/ui/button/toggle');
var self = require("sdk/self");
var {Cc, Ci, Cu} = require("chrome");
var system = require("sdk/system");
var sidebars = require("sdk/ui/sidebar");
let { search } = require("sdk/places/history");

const { pathFor } = require('sdk/system');
const path = require('sdk/fs/path');
const file = require('sdk/io/file');
const JiraApi = require('jira-module').JiraApi;
const MediatorApi = require('mediator-api').MediatorApi;


var jira;
var mediator;
var global_username;


var annotatorIsOn = false;
var firstClick = true;
var matchers = [];
var currentIssueKey = "";

simplePrefs.on("applyChanges", onPrefChange);

function onPrefChange() {
    console.log("Applied last changes");
    mediator = new MediatorApi(simplePrefs.prefs.mediatorProtocol,
        simplePrefs.prefs.mediatorHost,
        simplePrefs.prefs.mediatorPort,
        null,
        null,
        true);
}

function isAuthenticated() {
    //check if cookies are exist if not redirect to auth page
    var cookieManager = Cc["@mozilla.org/cookiemanager;1"].getService(Ci.nsICookieManager2);
    var count = cookieManager.getCookiesFromHost(simplePrefs.prefs.mediatorHost);

    while (count.hasMoreElements()) {
        var cookie = count.getNext().QueryInterface(Ci.nsICookie2);
        if (cookie.name === "crowd.token_key") {
            return true;
        }
        console.log(cookie.host + ";" + cookie.name + "=" + cookie.value + "\n");
    }
    firstClick = true;
    notifications.notify({
        title: 'Authentication error.',
        text: 'crowd.token_key not found'
    });
    return false;
}

const init = () => {
    console.log("Initializing jarvis plugin...");
    simpleStorage.storage.annotations = {};

    if (!isAuthenticated()) {
        tabs.open(simplePrefs.prefs.mediatorProtocol + "://" + simplePrefs.prefs.mediatorHost);
        return false;
    }

    if (!mediator) {
        mediator = new MediatorApi(simplePrefs.prefs.mediatorProtocol,
            simplePrefs.prefs.mediatorHost,
            simplePrefs.prefs.mediatorPort,
            null,
            null,
            true);
    }

    console.log("hello");

    simpleStorage.on("OverQuota", function () {
        notifications.notify({
            title: 'Storage space exceeded',
            text: 'Removing recent annotations'
        });
        while (simpleStorage.quotaUsage > 1)
            simpleStorage.storage.annotaions.pop();
    });

    mediator.listSessions(function (error, json) {
        console.log("All sessions response...");
        if (error) {
            console.error("Error: " + error);
        }
        else {
            if (json) {
                json.forEach(function (session) {
                    var captures = session.captures;
                    captures.forEach(function (annotation) {
                        var captureId = annotation.id;
                        if (!simpleStorage.storage.annotations[captureId]) {
                            simpleStorage.storage.annotations[captureId] = {};
                        }
                        simpleStorage.storage.annotations[captureId] = annotation;
                    });
                });
            }
            updateMatchers();
        }
    });
    return true;
};

init();

function Annotation(annotationText, anchor) {
    this.comment = annotationText;
    this.url = anchor[0];
    this.ancestorId = anchor[1];
    this.anchorText = anchor[2];
}

function handleNewAnnotation(annotationText, anchor, sessionKey, callback) {
    var newAnnotation = new Annotation(annotationText, anchor);
    console.log("Saving capture...");
    mediator.saveCapture(sessionKey, newAnnotation, function (error, json) {
        if (error) {
            console.error("Error : " + error);
        }
        else {
            console.log(JSON.stringify(json));
            let captureId = json.id;
            if (!simpleStorage.storage.annotations[captureId]) {
                simpleStorage.storage.annotations[captureId] = {};
            }
            simpleStorage.storage.annotations[captureId] = json;
            updateMatchers();
            callback(json);
        }
    });
}


function handleExistingAnnotationUpdate(annotationText, anchor, sessionKey, captureId, callback) {
    var newAnnotation = new Annotation(annotationText, anchor);
    newAnnotation.id = captureId;
    console.log("Updating capture...");
    mediator.updateCapture(sessionKey, newAnnotation, function (error, json) {
        if (error) {
            console.error("Error : " + error);
        }
        else {
            if (simpleStorage.storage.annotations[captureId]) {
                simpleStorage.storage.annotations[captureId].comment = annotationText;
                updateMatchers();
            }
        }
    });
}


function deleteAnnotation(annotationText, anchor, sessionKey, captureId, callback) {
    var newAnnotation = new Annotation(annotationText, anchor);
    newAnnotation.id = captureId;
    console.log("Deleting capture...");
    mediator.deleteCapture(sessionKey, newAnnotation, function (error, json) {
        if (error) {
            console.error("Error : " + error);
        }
        else {
            if (simpleStorage.storage.annotations[captureId]) {
                simpleStorage.storage.annotations[captureId] = {};
                updateMatchers();
            }
        }
    });
}


function onAttachWorker(annotationEditor, data) {
    annotationEditor.annotationAnchor = data;
    annotationEditor.show();
    console.log('On attach worker event...');
}

function onShowPopup(popup, data, X, Y) {
    popup.data = data;
    popup.show({position: {top: Y, left: X}});
    console.log('Show popup event...');
}

function onShowSidebar(sidebar, data) {
    sidebar.hide();
    sidebar.data = data;
    console.log( "sidebar.data " + sidebar.data )
    sidebar.show();
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
    console.log("Updating matchers...");
    matchers.forEach(function (matcher) {
        matcher.postMessage(simpleStorage.storage.annotations);
    });
}

function getUserIssues(jira, username) {
    jira.getUsersIssues(username, true, function (error, json) {
        if (error != null) {
            console.error("Could not retrieve " + username + "'s issues.");
            return;
        }
        return json;
    });
}

function getIssue(issueKey, panel) {
    console.log("Query for issue: " + issueKey);
    mediator.getIssue(issueKey, function (error, json) {
        if (error !== null) {
            console.error("Error: " + error);
            return;
        }
        console.log("Response: " + JSON.stringify(json));
        setIssue(json, panel);
    });
}

function setIssue(issue, panel) {
    console.log("Selecting issue: " + JSON.stringify(issue));
    if (issue) {
        panel.port.emit('set-issue', issue);
    }
    currentIssueKey = issue.key;
}

function listProjects(panel) {
    if (init()) {
        mediator.listProjects(function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
                return
            }
            panel.contentURL = data.url("login/selectProject.html");
            panel.port.emit("fill-project-combobox", json);
        });
    }
}

function disableEnableAnnotator(activate, button) {
    console.log('<<<activate/deactivate annotator: ' + activate + " " + annotatorIsOn);
    if (activate !== undefined) {
        annotatorIsOn = !activate;
    }
    var captureEnabled = toggleActivation();
    console.log("Now annotator is: " + captureEnabled);
    //to indicate that capture session is enabled or disabled
    if (captureEnabled) {
        button.icon = {
            "16": data.url('icon-16.png'),
            "32": "./icon-32.png",
            "64": "./icon-64.png"
        };
    }
    else {
        button.icon = {
            "16": data.url('icon-16-off.png'),
            "32": "./icon-32.png",
            "64": "./icon-64.png"
        };
    }
}


exports.main = function () {
    var selector = pageMod.PageMod({
        include: ['*'],
        contentScriptWhen: 'ready',
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
            data.url('selector.js')],

        onAttach: function (worker) {
            worker.postMessage(annotatorIsOn);
            selectors.push(worker);
            worker.port.on('show', function (data) {
                onAttachWorker(annotationEditor, data);
            });
            worker.port.on('initAnnotator', function (annotator) {
                console.log(annotator);
            });

            worker.port.on('show-popup', function (data, X, Y) {
                onShowPopup(popup, data, X, Y);
            });

            worker.port.on('page-scrooled', function (data, X, Y) {
                popup.hide();
            });

            worker.on('detach', function () {
                detachWorker(this, selectors);
            });
        }
    });

    var popup = panels.Panel({
        width: 25,
        height: 25,
        contentURL: data.url('popup/popup.html'),
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
            data.url('popup/popup.js'),
            data.url('jquery.highlight.js')]
    });

    popup.port.on('annotate-button-pressed', function () {
        onShowSidebar(sidebar, popup.data);
    });

    popup.port.on('highlight-button-pressed', function () {
        console.log("high is pressed");
        popup.port.emit("highlight", popup.data);
    });


    var annotationEditor = panels.Panel({
        width: 220,
        height: 220,
        contentURL: data.url('editor/annotation-editor.html'),
        contentScriptFile: data.url('editor/annotation-editor.js'),
        onMessage: function (annotationText) {
            if (annotationText) {
                console.log("annotationAnchor : " + this.annotationAnchor);
                console.log("annotationText : " + annotationText);
                console.log("currentIssueKey : " + currentIssueKey);
                handleNewAnnotation(annotationText, this.annotationAnchor, currentIssueKey, function (capture) {
                    console.log("Handle new annotation callback");
                    getIssue(capture.jiraKey, panel);
                });
            }
            annotationEditor.hide();
        },
        onShow: function () {
            this.postMessage('focus');
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

                annotation.baseUrl = data[0];
                annotation.content = data[1];
                annotation.textContent = data[2];

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
        width: 360,
        height: 210,
        contentURL: data.url('annotation/annotation.html'),
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
            data.url('annotation/annotation.js'),
            data.url('markdown/js/bootstrap-markdown-popup.js'),
            data.url('markdown/js/to-markdown.js'),
            data.url('markdown/js/markdown.js')],

        onShow: function () {
            this.postMessage(this.content);
        },
        onHide: function(){
            this.port.emit("hidePreview");
        }
    });


    annotation.port.on("updateAnnotation", function (data) {
        console.log( annotation.baseUrl );
        console.log( data  );
        console.log( annotation.textContent );
        console.log ( currentIssueKey );
        if ( currentIssueKey == "" ){
            notifications.notify({
                title: 'Warning!',
                text: 'Please select issue before updating annotation.'
            });
            return;
        }

        mediator.listSessionCaptures(currentIssueKey, function (error, json) {
            if (error) {
                console.error("Error : " + error);
            }
            else {
                for (var i = 0; i < json.length; i++){
                  if (json[i].anchorText == annotation.textContent ){
                    var anchor = [annotation.baseUrl, "content", annotation.textContent];
                    handleExistingAnnotationUpdate(data, anchor, currentIssueKey, json[i].id, function (capture) {
                        console.log("Handle existing annotation callback");
                        getIssue(currentIssueKey, panel);
                    });
                  }
                }
            }
        });
    });

    annotation.port.on("deleteAnnotation", function (data) {
        console.log( annotation.baseUrl );
        console.log( data  );
        console.log( annotation.textContent );
        console.log ( currentIssueKey );
        if ( currentIssueKey == "" ){
            notifications.notify({
                title: 'Warning!',
                text: 'Please select issue before deleting annotation.'
            });
            return;
        }

        mediator.listSessionCaptures(currentIssueKey, function (error, json) {
            if (error) {
                console.error("Error : " + error);
            }
            else {
                for (var i = 0; i < json.length; i++){
                  if (json[i].anchorText == annotation.textContent ){
                    var anchor = [annotation.baseUrl, "content", annotation.textContent];
                    deleteAnnotation(data, anchor, currentIssueKey, json[i].id, function (capture) {
                        console.log("Handle existing annotation callback");
                        getIssue(currentIssueKey, panel);
                    });
                  }
                }
            }
        });
    });


    annotation.port.on("mouseout-event", function () {
        annotation.hide();
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
                if (!isAuthenticated()) {
                    button.state('window', {checked: false});
                    tabs.open(simplePrefs.prefs.mediatorProtocol + "://" + simplePrefs.prefs.mediatorHost + ":" + simplePrefs.prefs.mediatorPort);
                }
                else {
                    panel.show({
                        position: button
                    });
                    if (firstClick) {
                        firstClick = false;
                        listProjects(panel);
                    }
                }
            }
        }
    });

    var panel = panels.Panel({
        width: 350,
        height: 500,
        contentScriptFile: [data.url('jquery-2.1.3.min.js'),
            data.url('issue-view/issue-view.js'),
            data.url('login/handleLogin.js'),
            data.url('list.min.js')
        ],
        onHide: function (state) {
            button.state('window', {checked: false});
        }
    });


    var sidebar = sidebars.Sidebar({
        id: 'my-sidebar',
        title: 'Annotations',
        width: 400,
        url: data.url("annotation/annotationSidebar.html"),
        onAttach: function (worker) {
            worker.port.on("ping", function() {
                console.log( "popup.data : " + popup.data )
                worker.port.emit("pong", popup.data);
            });
            worker.port.on("saveAnnotation", function(data) {
                console.log( data );
                handleNewAnnotation(data, popup.data, currentIssueKey, function (capture) {
                    console.log("data  " + data);
                    console.log("popup.data : " + popup.data);
                    console.log("currentIssueKey : " + currentIssueKey);
                    console.log("Handle new annotation callback");
                    getIssue(capture.jiraKey, panel);
                });
                sidebar.hide();
            }); 
        }
    });


    // When the panel is displayed it generated an event called
    // "show": we will listen for that event and when it happens,
    // send our own "show" event to the panel's script, so the
    // script can prepare the panel for display.
    panel.on("show", function () {
        console.log("Panel is shown...");
        panel.port.emit("show");
    });

    panel.port.on("stop-progress", function (issueId) {
        console.log("Stop progress.");
        mediator.stopSession(issueId, function (error, json) {
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

    panel.port.on('pause-progress', function (sessionKey) {
        console.log("Pause session.");
        mediator.pauseSession(sessionKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else {
                console.log("Session paused for Research: " + sessionKey);
                panel.port.emit("set-session", json);
            }
        });
    });

    panel.port.on('left-click', function (activate) {
        disableEnableAnnotator(activate, button);
    });

    panel.port.on('right-click', function () {
        console.log('show annotation list');
        annotationList.show();
    });

    panel.port.on("back-button-pressed", function (projectName) {
        console.log("back-button-pressed");
        disableEnableAnnotator(false, button);
        mediator.listProjectIssues(projectName, function (error, json) {
            if (error != null) {
                console.error("Could not retrieve " + global_username + "'s issues.");
                return;
            }
            panel.contentURL = data.url("login/research.html");
            panel.port.emit("fill-combo-box", json, projectName);
        });
    });

    panel.port.on("back-button-pressed-on-researchpage", function (projectKey) {
        console.log("back-button-pressed-on-researchpage");
        mediator.listProjects(function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
                return
            }

            panel.contentURL = data.url("login/selectProject.html");
            panel.port.emit("fill-project-combobox", json, projectKey);
        });
    });

    panel.port.on("back-button-pressed-on-project-selection-page", function () {
        console.log("back-button-pressed-on-project-selection-page");
        panel.contentURL = data.url("login/panel.html");
    });

    /**
     * Event triggered when issues was selected from combo box
     */
    panel.port.on("issue-selected", function (selectedIssueKey) {
        console.log("issue-selected");
        mediator.getIssue(selectedIssueKey, function (error, json) {
            if (error !== null) {
                console.error(error + ": Could not retrieve " + "'s issues.");
                return;
            }
            panel.contentURL = data.url("issue-view/issue-view.html");
            setIssue(json, panel);
        });
    });

    /**
     * Methods for navigating through issues and project
     */
    panel.port.on('select-issue', function (issueKey) {
        console.log("SelectedIssueKey: " + issueKey);

        getIssue(issueKey, panel);
    });

    /**
     * Get session full information, returns null if session wasn't started yet
     */
    panel.port.on('get-session', function (sessionKey) {
        console.log("get-session");
        mediator.getSession(sessionKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else if (json) {
                console.log(JSON.stringify(json));
                panel.port.emit('set-session', json);
                if (json.captures) {
                    var annotations = json.captures;
                    annotations.forEach(function (annotation) {
                        let captureId = annotation.id;
                        if (!simpleStorage.storage.annotations[captureId]) {
                            simpleStorage.storage.annotations[captureId] = {};
                        }
                        simpleStorage.storage.annotations[captureId] = annotation;
                    });
                    updateMatchers();
                }
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
        console.log("get-annotations");
        mediator.listSessionCaptures(sessionKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
            }
            else {
                panel.port.emit('set-annotations', json);
                json.forEach(function (annotation) {
                    let captureId = annotation.id;
                    if (!simpleStorage.storage.annotations[captureId]) {
                        simpleStorage.storage.annotations[captureId] = {};
                    }
                    simpleStorage.storage.annotations[captureId] = annotation;
                });
                updateMatchers();
            }
        });
    });

    panel.port.on("link-clicked", function (issueId) {
        tabs.open("http://test-jira.critical-factor.com/browse/" + issueId);
    });

    panel.port.on("navigate-to", function (url) {
        tabs.open(url);
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
                console.error("Could not retrieve " + global_username + "'s issues.");
                return;
            }
            panel.contentURL = data.url("login/research.html");
            panel.port.emit("fill-combo-box", json, projectName);
        });
    });

    // Listen for messages called "text-entered" coming from
    // the content script. The message payload is the text the user
    // entered.
    // In this implementation we'll just log the text to the console.
    panel.port.on("handle-login", function (username, password) {
        console.log(username + " " + password);
        global_username = username;
        listProjects(panel);
    });

    panel.port.on("build-hierarchy", function (storyKey) {
        console.log(storyKey);
        mediator.buildHierarchy(storyKey, function (error, json) {
            if (error !== null) {
                console.error("Error: " + error);
                return;
            }
            console.log("Success");
        });
    });


    panel.port.on("startStory", function (storyKey) {
        mediator.storyStart(storyKey, function (error, json) {
            if (error) {
                console.error("Error: " + error);
                return;
            }
            console.log("started story: " + storyKey);
            getIssue(storyKey, panel);
        });
    });

    panel.port.on("storySendApproval", function (storyKey) {
        mediator.storyApprovalRequest(storyKey, function (error) {
            if (error) {
                console.error("Error: " + error);
                return;
            }
            console.log("sent story approval request: " + storyKey);
            getIssue(storyKey, panel);
        });
    });

    panel.port.on("storyApprove", function (storyKey) {
        mediator.storyApprove(storyKey, function (error) {
            if (error) {
                console.error("Error: " + error);
                return;
            }
            console.log("approve story: " + storyKey);
            getIssue(storyKey, panel);
        });

    });

    panel.port.on("storyReject", function (storyKey) {
        mediator.storyReject(storyKey, function (error) {
            if (error) {
                console.error("Error: " + error);
                return;
            }
            console.log("reject story approval: " + storyKey);
            getIssue(storyKey, panel);
        });
    });

    panel.port.on("storyResolve", function (storyKey) {
        mediator.storyResolve(storyKey, function (error) {
            if (error) {
                console.error("Error: " + error);
                return;
            }
            console.log("resolve story approval: " + storyKey);
            getIssue(storyKey, panel);
        });
    });

    panel.port.on("transition-issue", function (transitionId) {
        mediator.transitionIssue(currentIssueKey, transitionId, function (error, json) {
            if (error) {
                console.log("Error couldn't transition issue: " + error);
                return;
            }
            console.log("transited issue");
            getIssue(currentIssueKey, panel);
        });
    });
};
