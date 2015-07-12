
self.port.emit("requestResource", "mfb/fbButtons.html", "body");

self.port.on("loadResource", function(resource, target) {
	console.log("resource is loaded");
	console.log(target);
	jQuery(target).append(resource);
});

enableAnnotator();
initializeFloatingMenu();

function enableAnnotator() {
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
}

function initializeFloatingMenu() {
	var panel = jQuery('#panel'),
		menu = jQuery('#jarvis-menu'),
		showcode = jQuery('#showcode'),
		selectFx = jQuery('#selections-fx'),
		selectPos = jQuery('#selections-pos'),
// demo defaults
		effect = 'mfb-slidein-spring',
		pos = 'mfb-component--br';

	showcode.on('click', _toggleCode);
	selectFx.on('change', switchEffect);
	selectPos.on('change', switchPos);

	function _toggleCode() {
		panel.classList.toggle('viewCode');
	}

	function switchEffect(e){
		effect = this.options[this.selectedIndex].value;
		renderMenu();
	}

	function switchPos(e){
		pos = this.options[this.selectedIndex].value;
		renderMenu();
	}

	function renderMenu() {
		menu.style.display = 'none';
		// ?:-)
		setTimeout(function() {
			menu.style.display = 'block';
			menu.className = pos + effect;
		},1);
	}
}