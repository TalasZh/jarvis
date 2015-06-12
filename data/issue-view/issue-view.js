const START = "Start";
const STOP = "Stop";
const PAUSE = "Pause";
const RESUME = "Resume";

const SESSION_STATUS = {
    IN_PROGRESS: "In Progress",
    CLOSED: "Closed",
    PAUSED: "Paused"
};

const ISSUE_TYPE = {
    RESEARCH: "Research",
    EPIC: "Epic",
    TASK: "Task",
    BUG: "Bug",
    STORY: "Story",
    IMPROVEMENT: "Improvement",
    PHASE: "Phase"
};

var buildHierarchyBtn = $("#buildHierarchy");
if (buildHierarchyBtn !== null) {
    buildHierarchyBtn.hide();
    buildHierarchyBtn.click(function () {
        var x = $("#issueNumber");
        var selectValue = x.text();
        console.log(selectValue);

        self.port.emit("build-hierarchy", selectValue);
    });
}

var startStop = $("#startStop");
if (startStop !== null) {
    startStop.click(function () {
        var x = $("#issueNumber");
        var selectValue = x.text();
        console.log(selectValue);
        if (startStop.prop("value") === START) {
            startStop.prop("value", STOP);
            console.log("Session started for " + selectValue + " at time : " + getDateTime());
            self.port.emit("start-progress", selectValue);
        }
        else {
            startStop.prop("value", START);
            console.log("Session stopped for " + selectValue + " at time : " + getDateTime());
            self.port.emit("stop-progress", selectValue);
        }
    });
}

var pauseResume = $("#pauseResume");
if (pauseResume !== null) {
    pauseResume.click(function () {
        var x = $("#issueNumber");
        var selectValue = x.text();
        if (pauseResume.prop("value") === "Pause") {
            pauseResume.prop("value", RESUME);
            startStop.prop("disabled", true);
            $("#annotator").prop("disabled", true);
            console.log("Session paused for " + selectValue + " at time : " + getDateTime());
            self.port.emit("pause-progress", selectValue);
        }
        else {
            pauseResume.prop("value", PAUSE);
            startStop.prop("disabled", false);
            $("#annotator").prop("disabled", false);
            console.log("Session resumed for " + selectValue + " at time : " + getDateTime());
            self.port.emit("start-progress", selectValue);
        }
    });
}

/**
 * handles new issue and substitutes all required fields
 */
self.port.on('set-issue', function (issue) {
    console.log("Setting issue: " + JSON.stringify(issue.type));
    if (!issue) {
        return;
    }
    switch (issue.type.name) {
        case ISSUE_TYPE.RESEARCH:
            prepareViewForResearch(issue);
            break;
        case ISSUE_TYPE.STORY:
            prepareViewForIssue(issue);
            $("#buildHierarchy").show();
            break;
        default:
            prepareViewForIssue(issue);
            break;
    }
    setGeneralFields(issue);
});

self.port.on('set-session', function (session) {
    console.log("Setting session");
    //Disable annotator for newly selected issue
    self.port.emit("left-click", false);

    let startStopBtn = $("#startStop");
    let pauseResumeBtn = $("#pauseResume");
    let annotator = $("#annotator");

    pushAnnotations(null);
    pauseResumeBtn.prop("disabled", false);
    startStopBtn.prop("disabled", false);
    annotator.prop("disabled", false);

    if (session) {
        startStopBtn.prop("value", STOP);
        pauseResumeBtn.show();
        annotator.show();

        switch (session.status) {
            case SESSION_STATUS.IN_PROGRESS:
                console.log(session.status);
                pauseResumeBtn.prop("value", PAUSE);

                self.port.emit("left-click", true);
                annotator.prop("class", "btn btn-primary btn-sm");
                break;
            case SESSION_STATUS.CLOSED:
                console.log(session.status);
                pauseResumeBtn.prop("disabled", true);
                startStopBtn.prop("disabled", true);
                annotator.prop("disabled", true);

                self.port.emit("left-click", false);
                annotator.prop("class", "btn btn-default btn-sm");
                break;
            case SESSION_STATUS.PAUSED:
                console.log(session.status);
                pauseResumeBtn.prop("value", RESUME);
                annotator.prop("disabled", true);
                startStopBtn.prop("disabled", true);

                self.port.emit("left-click", false);
                annotator.prop("class", "btn btn-default btn-sm");
                break;
        }
        let captures = session.captures;
        if (captures) {
            pushAnnotations(captures);
        }
        else {
            self.port.emit('get-annotations', session.key);
        }
    }
    else {
        self.port.emit("left-click", false);
        annotator.prop("class", "btn btn-primary btn-sm");
        startStopBtn.show();
        startStopBtn.prop("value", START);
        pauseResumeBtn.hide();
        annotator.hide();
    }
});
self.port.on('set-annotations', function (captures) {
    pushAnnotations(captures);
});

self.port.on('add-annotation', function (capture) {
    console.log("Adding annotation");
    let annotationList = $("#list-annotations");
    console.log(annotationList);
    let elem = annotationList.find("#" + capture.id);
    if (elem) {
        elem.replaceWith(buildAnnotationElement(capture));
    }
    else {
        annotationList.append(buildAnnotationElement(capture));
    }
});


function prepareViewForIssue(issue) {
    $("#annotations").hide();
    $("#phases").hide();
    $("#session-controls").hide();
    $("#buildHierarchy").hide();
    pushLinkedIssues(issue.links);
}

function prepareViewForResearch(issue) {
    $("#annotations").show();
    $("#session-controls").show();
    $("#phases").show();
    pushLinkedIssues(issue.links);
    setSession(issue.key);
}

function setSession(key) {
    self.port.emit('get-session', key);
}

function pushAnnotations(captures) {
    let annotationList = $("#list-annotations");
    annotationList.empty();
    if (!captures) {
        return;
    }
    for (let annotation of captures) {
        let elem = buildAnnotationElement(annotation);
        annotationList.append(elem);
        $(elem).attr("capture-url", annotation.url);
        $(elem).click(function () {
            self.port.emit("navigate-to", $(this).attr('capture-url'));
        });
    }
}

function pushLinkedIssues(links) {
    let issueList = $("#list-issues");
    issueList.empty();
    for (let item of links) {
        issueList.append(buildIssueLinkElement(item));
    }
    //notifies controller to select another issue
    $("a.issue-link").click(function () {
        self.port.emit('select-issue',
            $(this).text().trim()
        );
    });
}

function buildIssueLinkElement(linkItem) {
    let linkTypeSpan = "";
    switch (linkItem.type.name) {
        case ISSUE_TYPE.EPIC:
            linkTypeSpan = "  <span class=\"issueType label label-primary\">Epic</span>";
            break;
        case ISSUE_TYPE.PHASE:
            linkTypeSpan = "  <span class=\"issueType label label-success\">Phase</span>";
            break;
        case ISSUE_TYPE.RESEARCH:
            linkTypeSpan = "  <span class=\"issueType label label-info\">Research</span>";
            break;
        case ISSUE_TYPE.STORY:
            linkTypeSpan = "  <span class=\"issueType label label-warning\">Story</span>";
            break;
        case ISSUE_TYPE.BUG:
            linkTypeSpan = "  <span class=\"issueType label label-danger\">Bug</span>";
            break;
        default :
            linkTypeSpan = "  <span class=\"issueType label label-default\">Task</span>";
            break;
    }

    return "<li class=\"list-group-item\">" +
        "<a class= \"issue-link key\" href=\"#\">" + linkItem.key + "</a>" +
        linkTypeSpan +
        "<span class='projectKey' hidden>" + linkItem.projectKey + "</span>" +
        "</li>";
}

/**
 * {
 *  id: string,
 *  issueId: string,
 *  url: uri
 *  comment: string,
 *  ancestorId: string,
 *  anchorText: string
 * }
 * @param annotation
 */
function buildAnnotationElement(annotation) {
    return $.parseHTML("<a class=\"list-group-item\" id=" + annotation.id + ">" +
        "<div class=\"annotation-comment\">" +
        "<p class=\"list-group-item-heading\">" + annotation.comment + "</p>" +
        "</div>" +
        "    <blockquote class=\"list-group-item-text\">" +
        annotation.anchorText +
        "</blockquote>" +
        "</a>");
}

function setGeneralFields(issue) {
    $("a#issueLink").text(issue.key);
    $("#status").text(issue.status);
    $("#type").text(issue.type.name);
    $("#summary").text(issue.summary);
    $("#issueNumber").text(issue.key);
    buildCrumbs(issue);
}

function buildCrumbs(issue) {
    let issuePath = $("#issue-path");
    issuePath.empty();

    //add click event to navigate to projects view
    issuePath.append(breadcrumbItemBuilder("Projects", false, "projects"));


    //add click event to navigate to project view
    if (issue.projectKey && issuePath.projectKey !== issue.key) {
        issuePath.append(breadcrumbItemBuilder(issue.projectKey, false, "project"));
    }

    //specify class so that it calls/notifies controller event
    let navigateToIssue = (issueKey) => {
        self.port.emit('select-issue', issueKey);
    };

    if (issue.epic && issue.epic !== issue.key) {
        issuePath.append(breadcrumbItemBuilder(issue.epic, false, "issue"));
    }

    for (let link of issue.links) {
        if (link.type.name === "Story" && link.key !== issue.key) {
            issuePath.append(breadcrumbItemBuilder(link.key, false, "issue"));
            break;
        }
    }
    for (let link of issue.links) {
        if (link.type.name === "Phase" && link.key !== issue.key) {
            issuePath.append(breadcrumbItemBuilder(link.key, false, "issue"));
            break;
        }
    }
    issuePath.append(breadcrumbItemBuilder(issue.key, true));
    issuePath.find("a.issue").click(function () {
        self.port.emit('select-issue',
            $(this).text().trim()
        );
    });

    issuePath.find("a.project").click(function () {
        self.port.emit('back-button-pressed', issue.projectKey);
    });

    issuePath.find("a.projects").click(function () {
        self.port.emit('handle-login');
    });
}

function breadcrumbItemBuilder(key, active, clazz) {
    if (active) {
        return "<li class=\"active\">" +
                //"<span class=\"label label-primary\">" +
            key +
                //"</span>" +
            "</li>";
    }
    else {
        return "<li><a class=\"" + clazz +
            "\" href=\"#\">" +
            key +
            "</a></li>";
    }
}

console.log("Script is loaded");