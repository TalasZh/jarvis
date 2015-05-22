// var widgets = require('sdk/widget');
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
let { search } = require("sdk/places/history");

const { pathFor } = require('sdk/system');
const path = require('sdk/fs/path');
const file = require('sdk/io/file');
const JiraApi = require('jira-module').JiraApi;


var jira;
var global_username;


var annotatorIsOn = false;
var matchers = [];

const jira_init = () => {
    //JiraApi = require('jira-module').JiraApi;
    const jira = new JiraApi('http',
        'localhost',
        '2990',
        'admin',
        'admin',
        '2',
        true);
    var credentials = {
        username: "admin",
        password: "admin"
    };
    jira.startSession(credentials, function (error, json) {
        if (error !== null) {
            console.log(error);
        }
        else if (json !== undefined) {
            console.log(json);
        }
    });
    console.log("hello")
};

//jira_init();


if (!simpleStorage.storage.annotations)
    simpleStorage.storage.annotations = [];

simpleStorage.on("OverQuota", function () {
    notifications.notify({
        title: 'Storage space exceeded',
        text: 'Removing recent annotations'
    });
    while (simpleStorage.quotaUsage > 1)
        simpleStorage.storage.annotaions.pop();
});

function Annotation(annotationText, anchor) {
    this.annotationText = annotationText;
    this.url = anchor[0];
    this.ancestorId = anchor[1];
    this.anchorText = anchor[2];
}

function handleNewAnnotation(annotationText, anchor) {
    var newAnnotation = new Annotation(annotationText, anchor);
    simpleStorage.storage.annotations.push(newAnnotation);
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
            console.log("Could not retrieve " + username + "'s issues.");
            return;
        }
        return json;
    });
}


exports.main = function () {
    // var widget = widgets.Widget({
    // 	id: 'toggle-switch',
    // 	label: 'Annotator',
    // 	contentURL: data.url('widget/icon-64-off.png'),
    // 	contentScriptWhen: 'ready',
    // 	contentScriptFile: data.url('widget/widget.js')
    // });

    // widget.port.on('left-click', function() {
    // 	console.log('activate/deactivate');
    // 	widget.contentURL = toggleActivation() ?
    // 	data.url('widget/icon-64.png') :
    // 	data.url('widget/icon-64-off.png');
    // });

    // widget.port.on('right-click', function() {
    // 	console.log('show annotation list');
    // 	annotationList.show();
    // });

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
                handleNewAnnotation(annotationText, this.annotationAnchor);
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
            data.url('matcher.js')],
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
            "16": data.url('widget/icon-16.png'),
            "32": data.url('widget/icon-32.png'),
            "64": data.url('widget/icon-64.png')
        },
        onClick: function (state) {
            if (state.checked) {
                issueView.port.emit('set-issue', sampleIssue);
                issueView.show({
                    position: showIssue
                });
            }
        }
    });

    var views = [
        //data.url("issue-view/annotation-view.html"),
        //data.url("issue-view/task-view.html"),
        //data.url("issue-view/session-view.html"),
        //data.url("issue-view/phase-view.html"),
        data.url("issue-view/issue-view.html")];

    var order = 1;

    var sampleIssue = {
        key: "Issue-1",
        summary: "some summary",
        self: "https://api.jquery.com/empty/",
        type: "Epic",//Task, Session, Phase, Epic, Story etc...
        issueDescription: "some description",
        timeRemaining: "2h",
        assignee: "Jarvis",
        reporter: "Jarvis",
        sprint: "NA",
        components: "UI addon firefox",
        labels: "anyLabel someLabel thatLabel",
        status: "In Progress",
        resolution: "Open",
        fixVersion: "3.0.0",
        dateCreated: "01.02.2015",
        links: [
            {
                key: "Issue-2",
                summary: "another summary",
                type: "Task",
                linkType: "is blocked by"
            },
            {
                key: "Issue-3",
                summary: "another summary",
                type: "Phase",
                linkType: "is blocked by"
            },
            {
                key: "Issue-4",
                summary: "another summary",
                type: "Session",
                linkType: "is blocked by"
            },
            {
                key: "Issue-5",
                summary: "another summary",
                type: "Story",
                linkType: "is blocked by"
            },
            {
                key: "Issue-6",
                summary: "another summary",
                type: "Bug",
                linkType: "is blocked by"
            }
        ]
    };

    var issueView = panels.Panel({
        height: 600,
        width: 350,
        contentScriptFile: [data.url('issue-view/issue-view.js'),
            data.url('jquery-2.1.3.min.js')],
        contentURL: views[order % views.length],
        onShow: function () {
            order++;
            console.log(order);
        },
        onHide: function (state) {
            showIssue.state('window', {checked: false});
            this.contentURL = views[order % views.length];
        }
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
        contentURL: data.url("login/panel.html"),
        contentScriptFile: data.url('login/handleLogin.js'),
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
        var transitionJson = {
            transition: {
                id: 31
            }
        };
        jira.transitionIssue(issueId, transitionJson, function (error, message) {

            if (error !== null) {
                console.log(error);
            }

            if (message === "Success") {
                console.log("issue transition state is changed successfully.");
            }
            else {
                console.log("unsuccessfful");
            }
        });
    });


    panel.port.on("start-progress", function (issueId) {
        console.log("Start progress.");
        var transitionJson = {
            transition: {
                id: 11
            }
        };
        jira.transitionIssue(issueId, transitionJson, function (error, message) {

            if (error !== null) {
                console.log(error);
            }

            if (message === "Success") {
                console.log("issue transition state is changed successfully.");
            }
            else {
                console.log("unsuccessfful");
            }
        });

    });


    panel.port.on('left-click', function () {
        console.log('activate/deactivate');
        toggleActivation();
        // widget.contentURL = toggleActivation() ?
        // data.url('widget/icon-64.png') :
        // data.url('widget/icon-64-off.png');
    });

    panel.port.on('right-click', function () {
        console.log('show annotation list');
        annotationList.show();
    });


    panel.port.on("back-button-pressed", function () {
        panel.contentURL = data.url("login/research.html");
        jira.getUsersIssues(global_username, true, function (error, json) {
            if (error != null) {
                // console.log( error );
                console.log("Could not retrieve " + username + "'s issues.");
                return;
            }
            panel.port.emit("fill-combo-box", json);
        });
    });

    var selection = require("sdk/selection");
    console.log("helladf");
    if (selection.text) {
        console.log(selection.text);
        console.log("adfasdf");
    }


    panel.port.on("issue-selected", function (selectedIssueKey) {
        panel.contentURL = data.url("login/issueSelected.html");
        jira.getUsersIssues(global_username, true, function (error, json) {
            if (error != null) {
                // console.log( error );
                console.log("Could not retrieve " + username + "'s issues.");
                return;
            }
            for (var i = 0; i < json.issues.length; i++) {
                var issue = json.issues[i];
                if (issue.key == selectedIssueKey) {
                    panel.port.emit("issueKey", issue);
                    break;
                }
            }

        });
    });


    panel.port.on("link-clicked", function (issueId) {
        tabs.open("http://localhost:2990/jira/browse/" + issueId);
    });


    // Listen for messages called "text-entered" coming from
    // the content script. The message payload is the text the user
    // entered.
    // In this implementation we'll just log the text to the console.
    panel.port.on("handle-login", function (username, password) {
        console.log(username + " " + password);

        global_username = username;
        jira = new JiraApi('http',
            'localhost',
            '2990',
            username,
            password,
            '2',
            true);

        // jira find issue
        // jira.findIssue("JAP-1", function(error, response, json){
        // 	if ( response === 200 ) {
        // 		console.log( "adfadfadfadfadfadfa");
        // 		panel.contentURL = data.url("login/research.html");
        // 		// panel.contentScriptFile = data.url('login/test.js');
        // 	}
        // 	else {
        // 		console.log( "unsuccessfful" );
        // 	}
        // 	if(error !== null) {
        // 		console.log(error);
        // 	}
        // 	else if(json !== undefined) {
        // 		console.log(json);
        // 	}
        // });

        // list jira issues
        jira.getUsersIssues(username, true, function (error, json) {
            if (error != null) {
                // console.log( error );
                console.log("Could not retrieve " + username + "'s issues.");
                return;
            }
            panel.contentURL = data.url("login/research.html");
            panel.contentScriptFile = data.url('login/handleLogin.js');
            panel.port.emit("fill-combo-box", json);
        });

        // start session
        // jira.startSession('{"username": "admin","password": "admin"}', function(error, response, json){
        // 	console.log(response);
        // 	if ( response === 200 ) {
        // 		panel.contentURL = data.url("login/research.html");
        // 		panel.contentScriptFile = data.url('login/handleLogin.js');
        // 		panel.port.emit("fill-combo-box", json);
        // 	}
        // 	else {
        // 		console.log( "unsuccessfful" );
        // 	}
        // });


        // list transitions
        // jira.listTransitions('JAP-1',function(error, response, json){
        // 	if ( response === 200 ) {
        // 		console.log( "success" );
        // 		console.log( json );
        // 	}
        // 	else {
        // 		console.log( "unsuccessfful" );
        // 	}
        // });


        // issue transition
        // jira.transitionIssue('JAP-2', '{"transition":{"id":31}}', function(error, message){

        // 	if(error !== null) {
        // 		console.log(error);
        // 	}
        // 	if ( message === "Success" ) {
        // 		console.log( "issue transition state is changed successfully." );
        // 	}
        // 	else {
        // 		console.log( "unsuccessfful" );
        // 	}
        // });

    });
};
