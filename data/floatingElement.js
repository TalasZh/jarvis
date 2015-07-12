
$("head").append('<script src="resource://jpm-test/data/jquery-2.1.3.min.js" type="application/x-javascript"/>');

$("head").append('<script src="resource://jpm-test/data/annotator-full.1.2.10/annotator-full.min.js" type="application/x-javascript"/>');

$("head").append('<script src="resource://jpm-test/data/annotator.offline.min.js" type="application/x-javascript"/>');

//$("head").append('<script type="text/javascript" src="resource://jpm-test/data/materialize/js/materialize.min.js"></script>');

//$("head").append('<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.0/css/materialize.min.css">');

//$("head").append('<link type="text/css" rel="stylesheet" href="resource://jpm-test/data/materialize/custom-materialize.css"  media="screen,projection"/>');

$("head").append('<link rel="stylesheet" href="resource://jpm-test/data/annotator-full.1.2.10/annotator.min.css">');

//$("head").append('<link type="text/css" rel="stylesheet" href="resource://jpm-test/data/materialize/css/materialize.css"  media="screen,projection"/>');

//self.port.emit("requestResource", "floatingButton.html", "body");
//self.port.emit("requestResource", "fontLoader.html", "head");
// self.port.emit("requestResource", "custom-font.css", "head");

self.port.emit("requestResource", "mfb/libsImport.html", "head");
self.port.emit("requestResource", "mfb/fbButtons.html", "body");

self.port.on("loadResource", function(resource, target) {
	console.log("resource is loaded");
	console.log(target);
	jQuery(target).append(resource);
});


var content = jQuery("body").annotator();
content.annotator('addPlugin', 'Offline', {
	online:  function () {
		jQuery("#status").text("Online");
	},
	offline: function () {
		jQuery("#status").text("Offline");
	}
});

var annotator = content.data('annotator');

jQuery("#clear-storage").click(function () {
	  if (annotator) {
		  annotator.plugins.Offline.store.clear();
	  }
});

