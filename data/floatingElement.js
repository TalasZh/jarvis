(function (window, document, undefined) {
    jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/index.css">');
    jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/mfb.css">');
    jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/custom-materialize.css">');

    var annotatorIsOn = false;

    self.port.emit("requestResource", "mfb/fbButtons.html", "body");

    self.port.on("loadResource", function (resource, target) {
        console.log("resource is loaded");
        console.log(target);
        jQuery(target).append(resource);
        var annotatorControl = jQuery(".mfb-component__button--child.btn-jarvis-annotation-control");
        annotatorControl.on("click", function () {
            annotatorIsOn = !annotatorIsOn;
            console.log("Annotator status: " + annotatorIsOn);
            //enableAnnotator();

        });

        var researchList = jQuery(".mfb-component__button--child.btn-jarvis-list-researches");
        researchList.on("click", function () {
            console.log("research list is clicked");
            //jQuery(".jarvis-research-list").data("jarvis-list-state", "expand");
            self.port.emit("getResearchList");
            //jQuery("#research-list").slideToggle();
        });
        //jQuery("#research-list").slideToggle();
        enableAnnotator();
    });

    self.port.on("setResearches", function (error, researches) {
        var researchList = jQuery(".jarvis-research-list dl");
        researchList.empty();
        if (error) {
            //alert("Please make sure that service is working now. ");
            alert("Error: \n" + error);
        }
        else {
            for (let research of researches) {
                researchList.append("<dt >" + research.key + "</dt>");
            }
            if (researches.length == 0) {
                alert("No Research issues are pulled...");
            }
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
