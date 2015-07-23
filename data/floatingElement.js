(function (window, document, undefined) {
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/index.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/mfb.css">');
    //jQuery("head").append('<link rel="stylesheet" href="resource://jarvis-addon/data/mfb/custom.css">');

    var annotatorIsOn = false;
    var options = {valueNames: ["jarvis-issue-key", "jarvis-issue-type"]};
    var researchIssuesList = new List("jarvis-research-list-ctrl", options);

    self.port.emit("requestResource", "mfb/fbButtons.html", "body");

    self.port.on("loadResource", function (resource, target) {
        console.log("resource is loaded");
        console.log(target);
        jQuery(target).append(resource);
        var annotatorControl = jQuery("#btn-jarvis-annotation-control");
        annotatorControl.on("click", function () {
            annotatorIsOn = !annotatorIsOn;
            console.log("Annotator status: " + annotatorIsOn);
        });

        var researchList = jQuery("#btn-jarvis-list-researches");
        researchList.on("click", function () {
            console.log("research list is clicked");
            self.port.emit("getResearchList");

            var researchIssueList = jQuery("#jarvis-research-list-ctrl");
            var currentState = researchIssueList.attr("data-jarvis-list-state");
            if (currentState === "collapse") {
                jQuery(researchIssueList).attr("data-jarvis-list-state", "expand");
            }
            else {
                jQuery(researchIssueList).attr("data-jarvis-list-state", "collapse");//, "jarvis-list-state", "collapse");
            }
            console.log(currentState);
        });

        var researchListCtrl = jQuery("#jarvis-research-list-ctrl");
        researchListCtrl.on("mouseleave", function () {
            setTimeout(function () {
                var researchIssueList = jQuery("#jarvis-research-list-ctrl");
                if (!jQuery("#btn-jarvis-list-researches").is(":hover")) {
                    researchIssueList.attr("data-jarvis-list-state", "collapse");
                }
            }, 1000);
        });

        $("#searchResearchIssues").keyup(function () {
            var criteria = $(this).val();
            if (criteria) {
                researchIssuesList.search(criteria);
            }
            else {
                researchIssuesList.search();
            }
        });

        researchIssuesList = new List("jarvis-research-list-ctrl", options);
        researchIssuesList.sort("jarvis-issue-key", {order: "asc"});

        //enableAnnotator();
    });

    self.port.on("setResearches", function (error, researches) {
        console.log("Setting research issues...");
        var researchList = jQuery("#jarvis-research-list-ctrl").find("dl.list");
        researchList.empty();
        if (error) {
            //alert("Please make sure that service is working now. ");
            alert("Error: \n" + error);
        }
        else {
            for (let research of researches) {
                //<dt>
                //  <a class="issueKey">KEY-1</a>
                //  <span class="issueType">Issue</span>
                //</dt>
                console.log(research);
                researchList.append("<dt > <a class='jarvis-issue-key'>" +
                    research.key +
                    "</a>" +
                    "<span class='jarvis-issue-type'>" +
                    research.fields.issuetype.name +
                    "</span>" +
                    "</dt>");
            }
            if (researches.length == 0) {
                alert("No Research issues are pulled...");
            }
            researchIssuesList = new List("jarvis-research-list-ctrl", options);
            researchIssuesList.sort("jarvis-issue-key", {order: "asc"});
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
