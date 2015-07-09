// var self = require('sdk/self');

// // a dummy function, to show how tests work.
// // to see how to test this function, look at test/test-index.js
// function dummy(text, callback) {
//   callback(text);
// }

// exports.dummy = dummy;

var { ActionButton } = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");
var data = require('sdk/self').data;
var pageMod = require("sdk/page-mod");
var floatingCtrls = [];

var button = ActionButton({
  id: "my-button",
  label: "Visit Mozilla",
  icon: {
    "16": data.url('icon-16.png'),
    "32": data.url('icon-32.png'),
    "64": data.url('icon-64.png')
  },
  onClick: handleClick
});

function handleClick(state) {
  tabs.open("http://www.mozilla.org/");
}

exports.main = function(options) {

	var floatingCtrl = pageMod.PageMod({
		include: ["*"],
		contentScriptFile: [data.url("jquery-2.1.3.min.js"), 
			data.url("annotator-full.1.2.10/annotator-full.min.js"),
			data.url("annotator.offline.min.js"),
			data.url("floatingElement.js")],
		onAttach: function(worker) {
			worker.postMessage({some: "options"});
			worker.port.on("requestResource", function(resourceName){
				console.log(data.url(resourceName));
				worker.port.emit("loadResource", data.load(resourceName));
			});
		}
	});
}
