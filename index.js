//var widgets = require('sdk/widget');
var data = require('sdk/self').data;
var pageMod = require('sdk/page-mod');
var selectors = [];
var panels = require('sdk/panel');
var simpleStorage = require('sdk/simple-storage');
var notifications = require('sdk/notifications');

var { ToggleButton } = require('sdk/ui/button/toggle');
// var panels = require("sdk/panel");
var self = require("sdk/self");
var {Cc, Ci, Cu} = require("chrome");
var system = require("sdk/system");
let { search } = require("sdk/places/history");

const { pathFor } = require('sdk/system');
const path = require('sdk/fs/path');
const file = require('sdk/io/file');



var annotatorIsOn = false;
var matchers = [];

const jira_init = () => {
	JiraApi = require('jira-module').JiraApi;
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
	jira.startSession(credentials, function(error, json){
		if(error !== null) {
			console.log(error);
		}
		else if(json !== undefined) {
			console.log(json);
		}
	});
	console.log("hello")
}

//jira_init();


if (!simpleStorage.storage.annotations)
	simpleStorage.storage.annotations = [];

simpleStorage.on("OverQuota", function(){
	notifications.notify({
		title: 'Storage space exceeded',
		text: 'Removing recent annotations'
	});
	while(simpleStorage.quotaUsage > 1)
		simpleStorage.storage.annotaions.pop();
});

function Annotation (annotationText, anchor) {
	this.annotationText = annotationText;
	this.url = anchor[0];
	this.ancestorId = anchor[1];
	this.anchorText = anchor[2];
}

function handleNewAnnotation (annotationText, anchor) {
	var newAnnotation = new Annotation(annotationText, anchor);
	simpleStorage.storage.annotations.push(newAnnotation);
	updateMatchers();
}

function onAttachWorker(annotationEditor, data) {
	annotationEditor.annotationAnchor = data;
	annotationEditor.show();
	console.log('On attach worker event...');

  // var { jquery } = require('./jquery-2.1.3.js');
  // var { annotator } = require('./annotator-full.min.js');
  // var app = new annotator.App();
}

function detachWorker(worker, workerArray) {
	var index = workerArray.indexOf(worker);
	if(index != -1) {
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
	matchers.forEach(function(matcher){
		matcher.postMessage(simpleStorage.storage.annotations);
	});
}


exports.main = function() {

	//var widget = widgets.Widget({
	//	id: 'toggle-switch',
	//	label: 'Annotator',
	//	contentURL: data.url('widget/icon-64-off.png'),
	//	contentScriptWhen: 'ready',
	//	contentScriptFile: data.url('widget/widget.js')
	//});
	var widget = ToggleButton({
		id: "toggle-switch",
		label: "Jarvis",
		icon: data.url('widget/icon-64-off.png'),
		onClick: handleToogleSwitchClick
	});

	function handleToogleSwitchClick(state) {
		console.log('activate/deactive');
		widget.icon = toggleActivation() ? data.url('widget/icon-64.png') : 
			data.url('widget/icon-64-off.png');
	}

	//widget.port.on('left-click', function() {
	//	console.log('activate/deactivate');
	//	widget.contentURL = toggleActivation() ?
	//	data.url('widget/icon-64.png') :
	//	data.url('widget/icon-64-off.png');
	//});

	//widget.port.on('right-click', function() {
	//	console.log('show annotation list');
	//	annotationList.show();
	//});

	var selector = pageMod.PageMod({
		include: ['*'],
		contentScriptWhen: 'ready',
		contentScriptFile: [data.url('jquery-2.1.3.min.js'),
		data.url('selector.js'),
		data.url('annotator_lib/annotator-full.min.js')],

		contentStyleFile: [data.url('annotator_lib/annotator.min.css')],

		onAttach: function(worker) {
	    // console.log(jira);
	    worker.postMessage(annotatorIsOn);
	    selectors.push(worker);
	    worker.port.on('show', function(data) {
	    	onAttachWorker(annotationEditor, data);
	    });
	    worker.port.on('initAnnotator', function(annotator) {
	      // var app = annotator.App();
	      console.log( annotator );
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
		onMessage: function(annotationText) {
			console.log("******************Triggered log");
			if (annotationText) {
				console.log(this.annotationAnchor);
				console.log(annotationText);
				handleNewAnnotation(annotationText, this.annotationAnchor);
			}
			annotationEditor.hide();
		},
		onShow: function() {
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
		onShow: function(){
			this.postMessage(simpleStorage.storage.annotations);
		},
		onMessage: function(message) {
			require('sdk/tabs').open(message);
		}
	});

	var matcher = pageMod.PageMod({
		include: ['*'],
		contentScriptWhen: 'ready',
		contentScriptFile: [data.url('jquery-2.1.3.min.js'),
		data.url('matcher.js')],
		onAttach: function(worker) {
			if(simpleStorage.storage.annotations) {
				worker.postMessage(simpleStorage.storage.annotations);
			}
			worker.port.on('show', function(data) {
				annotation.content = data;
				annotation.show();
			});
			worker.port.on('hide', function() {
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
		onShow: function() {
			this.postMessage(this.content);
		}
	});


	var button = ToggleButton({
		id: "my-button",
		label: "Jarvis",
		icon: {
			"16": "./icon-16.png",
			"32": "./icon-32.png",
			"64": "./icon-64.png"
		},
		onChange: function(state) {
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
		onHide: function(state) {
			button.state('window', {checked: false});
		}
	});

	// When the panel is displayed it generated an event called
	// "show": we will listen for that event and when it happens,
	// send our own "show" event to the panel's script, so the
	// script can prepare the panel for display.
	panel.on("show", function() {
	  panel.port.emit("show");
	});


	panel.port.on("back-button-pressed", function() {
		panel.contentURL = data.url("login/panel.html");
	});


	// Listen for messages called "text-entered" coming from
	// the content script. The message payload is the text the user
	// entered.
	// In this implementation we'll just log the text to the console.
	panel.port.on("handle-login", function (username, password) {
	  console.log(username + " " + password );

		JiraApi = require('jira-module').JiraApi;
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
			jira.getUsersIssues(username, true, function(error, json){
				if ( error ) {
					console.log( "unsuccessfful " + error );
				}
				else {
					panel.contentURL = data.url("login/research.html");
					panel.contentScriptFile = data.url('login/handleLogin.js');
					panel.port.emit("fill-combo-box", json);
				}
			});
	});
}
