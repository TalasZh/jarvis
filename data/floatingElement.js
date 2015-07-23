(function(window, document, undefined){
	var annotatorIsOn = false;

	self.port.emit("requestResource", "mfb/fbButtons.html", "body");

	self.port.on("loadResource", function (resource, target) {
		console.log("resource is loaded");
		console.log(target);
		jQuery(target).append(resource);
		var annotatorControl = jQuery("#jarvis-annotation-control");
		annotatorControl.on("click", function () {
			annotatorIsOn = !annotatorIsOn;
			console.log("Annotator status: " + annotatorIsOn);
			//enableAnnotator();

		});

		var researchList = jQuery("#list-researches");
		researchList.on("click", function () {
			console.log("research list is clicked");
			self.port.emit("getResearchList");
			jQuery("#research-list").slideToggle();
		});
		jQuery("#research-list").slideToggle();
		enableAnnotator();
	});

	self.port.on("setResearches", function (researches) {
		var researchList = jQuery("#research-list");
		researchList.empty();
		for (let research of researches) {
			researchList.append("<dt>" + research.key + "</dt>");
		}
	});


	function enableAnnotator() {
		var content = jQuery("body").annotator({
			readOnly: annotatorIsOn
		});

		content.annotator('addPlugin', 'Offline', {
			online: function () {
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
	}
})(window, document);
