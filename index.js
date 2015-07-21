// var self = require('sdk/self');

// // a dummy function, to show how tests work.
// // to see how to test this function, look at test/test-index.js
// function dummy(text, callback) {
//   callback(text);
// }

var { ActionButton } = require('sdk/ui/button/action');
var tabs = require("sdk/tabs");
var data = require('sdk/self').data;
var pageMod = require("sdk/page-mod");
var floatingCtrls = [];
var researches = null;

var JiraApi = require("jira-module").JiraApi;

var jira = new JiraApi("https://jira.subutai.io", "2", false, false);
jira.searchJira('issuetype in (Story) AND resolution = Unresolved AND assignee in (currentUser()) ORDER BY updatedDate DESC', null, function (error, json) {
    if (error) {
        console.error("Request completed with errors: " + error);
    }
    else {
        researches = json.issues;
        console.log(researches);
    }
});


var button = ActionButton({
    id: "jarvis-activator",
    label: "Enable/Disable Jarvis",
    icon: {
        "16": data.url('jarvis_logo_16x16_cropped.png'),
        "32": data.url('jarvis_logo_32x32_cropped.png'),
        "64": data.url('jarvis_logo_64x64_cropped.png')
    },
    onClick: handleClick
});


function handleClick(state) {
    tabs.open("http://www.mozilla.org/");
}

exports.main = function (options) {

    var floatingCtrl = pageMod.PageMod({
        include: ["*"],
        attachTo: ["top"],
        contentStyleFile: [data.url("mfb/custom-materialize.css"),
            data.url("annotator-full.1.2.10/annotator.min.css"),
            data.url("mfb/mfb.css"),
            data.url("mfb/index.css")
        ],
        contentScriptWhen: "end",
        contentScriptFile: [data.url("jquery-2.1.3.min.js"),
            data.url("annotator-full.1.2.10/annotator-full.min.js"),
            data.url("annotator.offline.min.js"),
            //data.url("annotator.min.js"),
            data.url("mfb/mfb.js"),
            data.url("mfb/modernizr.touch.js"),
            data.url("floatingElement.js")],
        onAttach: function (worker) {

            console.log("Worker initialized");
            worker.postMessage({some: "options"});
            worker.port.on("requestResource", function (resourceName, target) {
                console.log(data.url(resourceName));
                console.log(target);
                worker.port.emit("loadResource", data.load(resourceName), target);
            });

            worker.port.on("getResearchList", function () {
                if (researches) {
                    console.log(researches);
                    worker.port.emit("setResearches", researches);
                }
            });
        }
    });
}
