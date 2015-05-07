var widgets = require('sdk/widget');
var data = require('sdk/self').data;
var pageMod = require('sdk/page-mod');
var selectors = [];
var panels = require('sdk/panel');
var simpleStorage = require('sdk/simple-storage');
var notifications = require('sdk/notifications');

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
	jira.findIssue("JAP-1", function(error, json){
		if(error !== null) {
			console.log(error);
		}
		else if(json !== undefined) {
			console.log(json);
		}
	});
	console.log(jira.makeUri('/issues/'));
}

jira_init();

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

  var widget = widgets.Widget({
    id: 'toggle-switch',
    label: 'Annotator',
    contentURL: data.url('widget/icon-64-off.png'),
    contentScriptWhen: 'ready',
    contentScriptFile: data.url('widget/widget.js')
  });

  widget.port.on('left-click', function() {
    console.log('activate/deactivate');
    widget.contentURL = toggleActivation() ?
              data.url('widget/icon-64.png') :
              data.url('widget/icon-64-off.png');
  });

  widget.port.on('right-click', function() {
      console.log('show annotation list');
      annotationList.show();
  });
  
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

  // var { jira } = require('./node-jira/index.js');

  // var { annotator } = [require('./annotator-full.min.js'), require('sdk/self')];

  // var app = new annotator.App();
}
