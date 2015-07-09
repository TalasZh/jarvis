
//document.body.innerHTML = "<h1>Page matches ruleset</h1>";

//self.port.on("message", function onMessage(options) {
//	init();
//	$(function() {
//		var offsetPixels = 700;
//		$(window).scroll(function() {
//			if($(window).scrollTop() > offsetPixels) {
//				$('.scrollingBox').css({
//					'position': 'fixed',
//					'top': '15px'
//				});
//			}
//			else {
//				$('.scrollingBox').css({
//					'position':'static'
//				});
//			}
//		});
//	});
//});
//
////init();
//

function init() {
	$("body").load("floatingElement.html");
	$.get("floatingElement.html", function (data) {
		alert("Load was performed...");
		console.log(data);
	});
}

$("head").append('<link rel="stylesheet" href="resource://jpm-test/data/annotator-full.1.2.10/annotator.min.css">');

$("head").append('<script src="resource://jpm-test/data/annotator-full.1.2.10/annotator-full.min.js" type="application/x-javascript"/>');

$("head").append('<script src="resource://jpm-test/data/annotator.offline.min.js" type="application/x-javascript"/>');

$("head").append('<link type="text/css" rel="stylesheet" href="resource://jpm-test/data/materialize/css/materialize.min.css"  media="screen,projection"/>');

$("head").append('<script type="text/javascript" src="resource://jpm-test/data/materialize/js/materialize.min.js"></script>');

self.port.emit("requestResource", "floatingButton.html");

self.port.on("loadResource", function(resource) {
	console.log("resource is loaded");
	$("body").append(resource);

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

