var annotationItemTemplate = "<a class=\"list-group-item\">\
<p class=\"list-group-item-heading\">Annotation1</p>\
    <blockquote class=\"list-group-item-text\">Some comment or annotated text</blockquote>\
</a>";

var issueItemTemplate = "<li class=\"list-group-item\">\
<a href=\"issue-view.html\">\
    Cras justo odio <span class=\"label label-success\">Epic</span>\
    </a>\
    </li>";

self.port.on('set-issue', function (issue) {
    console.log("Sample issue: " + JSON.stringify(issue));
    switch (issue.type) {
        case "Task":
        case "Phase":
        case "Epic":
        case "Story":
            activateIssueControls(issue);
            break;
        case "Session":
            break;
        default:
            activateIssueControls(issue);
            break;
    }
    setGeneralFields(issue);
});

function activateIssueControls(issue) {
    console.log("trying to hide some controls");
    $("#annotations").remove();
    $("#phases").remove();

    let issueList = $("#list-issues");
    issueList.empty();
    for (let item of issue.links) {
        issueList.append(buildLinkItemTemplate(item));
    }
}

function buildLinkItemTemplate(linkItem) {

//<span class="label label-default">Default</span> ==> Task
//<span class="label label-primary">Primary</span> ==> Epic
//<span class="label label-success">Success</span> ==> Phase
//<span class="label label-info">Info</span> ==> Session
//<span class="label label-warning">Warning</span> ==> Story
//<span class="label label-danger">Danger</span> ==> Bug

    let linkTypeSpan = "";
    switch (linkItem.type) {
        case "Epic":
            linkTypeSpan = "  <span class=\"label label-primary pull-right\">Epic</span>";
            break;
        case "Phase":
            linkTypeSpan = "  <span class=\"label label-success\">Phase</span>";
            break;
        case "Session":
            linkTypeSpan = "  <span class=\"label label-info\">Phase</span>";
            break;
        case "Story":
            linkTypeSpan = "  <span class=\"label label-warning\">Story</span>";
            break;
        case "Bug":
            linkTypeSpan = "  <span class=\"label label-danger\">Bug</span>";
            break;
        default :
            linkTypeSpan = "  <span class=\"label label-default\">Task</span>";
            break;
    }

    return "<li class=\"list-group-item\">" +
        "<a href=\"#\">" + linkItem.key + linkTypeSpan + "</a>" +
        "</li>";
}

function setGeneralFields(issue) {
    $("a#issueLink").attr("href", issue.self);
    $("#status").text(issue.status);
    $("#type").text(issue.status);
    $("#summary").text(issue.summary);
}

console.log("Script is loaded");