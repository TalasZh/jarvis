// var self = require('sdk/self');

// // a dummy function, to show how tests work.
// // to see how to test this function, look at test/test-index.js
// function dummy(text, callback) {
//   callback(text);
// }

// exports.dummy = dummy;

var {Cc, Cu, Ci} = require('chrome');
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

function makeURI(aURL, aOriginCharset, aBaseURI) {
  var ioService = Cc["@mozilla.org/network/io-service;1"].getService(Ci.nsIIOService);
  return ioService.newURI(aURL, aOriginCharset, aBaseURI);
}


exports.main = function(options) {

	var floatingCtrl = pageMod.PageMod({
		include: ["*"],
		//contentStyleFile: [data.url("materialize/custom-materialize.css")],
		// contentStyle: '@font-face{font-family: "Material-Design-Icons";src: url("'+ data.url("materialize/font/material-design-icons/Material-Design-Icons.woff") + '") format("woff"); font-weight: normal;	font-style: normal;	}',
		contentScriptWhen: "ready",
		contentScriptFile: [data.url("jquery-2.1.3.min.js"),
			data.url("floatingElement.js")],
		onAttach: function(worker) {
			
			// var sss = Cc['@mozilla.org/content/style-sheet-service;1'].getService(Ci.nsIStyleSheetService);
			// var ios = Cc['@mozilla.org/network/io-service;1'].getService(Ci.nsIIOService);

			// var css = "@font-face{" +
			// 	"font-family: 'Material-Design-Icons';" +
			//     "src:url(" + data.url('materialize/font/material-design-icons/Material-Design-Icons.woff') + ") format('woff')," +
			//     "}";

			// //var cssEnc = 'data:text/css;base64,' + window.btoa(css);
			// var cssEnc = encodeURIComponent(css);
			// var cssUri = makeURI('data:text/css,' + cssEnc);
			// sss.loadAndRegisterSheet(cssUri, sss.USER_SHEET);

			worker.postMessage({some: "options"});
			worker.port.on("requestResource", function(resourceName, target){
				console.log(data.url(resourceName));
				console.log(target);
				worker.port.emit("loadResource", data.load(resourceName), target);
			});
		}
	});
}
