self.port.emit("requestResource", "mfb/fbButtons.html", "body");

self.port.on("loadResource", function (resource, target) {
    console.log("resource is loaded");
    console.log(target);
    jQuery(target).append(resource);
    var annotatorControl = jQuery("#jarvis-annotation-control");
    annotatorControl.on("click", function () {
        annotatorIsOn = !annotatorIsOn;
        console.log("Annotator status: " + annotatorIsOn);
        enableAnnotator();
    });

	var researchList = jQuery("#research-list");
	researchList.on("click", function() {
		self.port.emit("getResearchList");
	});
    enableAnnotator();
});

self.port.on("setResearches", function(researches) {
	var researchList = jQuery("#researchList");
	researchList.empty();
	for(var i = 0; i)
});

var annotatorIsOn = false;

initializeFloatingMenu();

function enableAnnotator() {
    //var app = new annotator.App();
    //console.log(app);
    //app.include(annotator.ui.main);
    //app.include(annotator.storage.http);
    //app.start();
    //alert("Some message for ya");


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

    function switchEffect(e) {
        effect = this.options[this.selectedIndex].value;
        renderMenu();
    }

    function switchPos(e) {
        pos = this.options[this.selectedIndex].value;
        renderMenu();
    }

    function renderMenu() {
        menu.style.display = 'none';
        // ?:-)
        setTimeout(function () {
            menu.style.display = 'block';
            menu.className = pos + effect;
        }, 1);
    }
}
