var widgets = require('sdk/widget');
var data = require('sdk/self').data;
var pageMod = require('sdk/page-mod');
var selectors = [];
var panels = require('sdk/panel');

var annotatorIsOn = false;

function toggleActivation() {
  annotatorIsOn = !annotatorIsOn;
  return annotatorIsOn;
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


exports.main = function() {

  var widget = widgets.Widget({
    id: 'toggle-switch',
    label: 'Annotator',
    contentURL: data.url('widget/pencil-off.png'),
    contentScriptWhen: 'ready',
    contentScriptFile: data.url('widget/widget.js')
  });

  widget.port.on('left-click', function() {
    console.log('activate/deactivate');
    widget.contentURL = toggleActivation() ?
              data.url('widget/pencil-on.png') :
              data.url('widget/pencil-off.png');
  });

  widget.port.on('right-click', function() {
      console.log('show annotation list');
  });
  
  var selector = pageMod.PageMod({
  include: ['*'],
  contentScriptWhen: 'ready',
  contentScriptFile: [data.url('jquery-2.1.3.min.js'),
                      data.url('selector.js'),
                      data.url('annotator_lib/annotator-full.min.js')],
  contentStyleFile: [data.url('annotator_lib/annotator.min.css')],
  onAttach: function(worker) {
    worker.postMessage(annotatorIsOn);
    selectors.push(worker);
    worker.port.on('show', function(data) {
      //console.log(data);
      annotationEditor.annotationAnchor = data;
      annotationEditor.show();
      console.log(annotationEditor.annotationAnchor);
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
    console.log(annotationText);
    if (annotationText) {
      console.log(this.annotationAnchor);
      console.log(annotationText);
    }
    annotationEditor.hide();
  },
  onShow: function() {
    this.postMessage('focus');
  }
  });
}
