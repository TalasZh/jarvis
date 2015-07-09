
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
self.port.on("loadResource", function(resource) {
	console.log("resource is loaded");
	$("body").append(resource);

});
self.port.emit("requestResource", "floatingElement.html");

