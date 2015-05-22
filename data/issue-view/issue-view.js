var annotationItemTemplate = "<a class=\"list-group-item\">\
<p class=\"list-group-item-heading\">Annotation1</p>\
    <blockquote class=\"list-group-item-text\">Some comment or annotated text</blockquote>\
</a>";

var issueItemTemplate = "<li class=\"list-group-item\">\
<a href=\"issue-view.html\">\
    Cras justo odio <span class=\"label label-success\">Epic</span>\
    </a>\
    </li>";

//handles new issue and substitutes all required fields
self.port.on('set-issue', function (issue) {
    //console.log("Sample issue: " + JSON.stringify(issue));
    switch (issue.type) {
        case "Task":
        case "Phase":
        case "Epic":
        case "Story":
            prepareViewForIssue(issue);
            break;
        case "Session":
            prepareViewForSession(issue);
            break;
        default:
            prepareViewForIssue(issue);
            break;
    }
    setGeneralFields(issue);
});

function prepareViewForIssue(issue) {
    $("#annotations").hide();
    $("#phases").hide();

    pushLinkedIssues(issue.links);
}

function prepareViewForSession(issue) {
    $("#annotations").show();
    $("#phases").show();
    pushLinkedIssues(issue.links);
    pushAnnotations(issue.key);
}

function pushAnnotations(key) {
    self.port.emit('get-annotations', key);
    self.port.on('set-annotations', function (annotations) {
        let annotationList = $("#list-annotations");
        annotationList.empty();
        for (let annotation of annotations) {
            annotationList.append(buildAnnotationElement(annotation));
        }
    });
}

function pushLinkedIssues(links) {
    let issueList = $("#list-issues");
    issueList.empty();
    for (let item of links) {
        issueList.append(buildIssueLinkElement(item));
    }
    //notifies controller to select another issue
    $("a.issue-link").click(function () {
        $("#list-issues").empty();
        $("#list-annotations").empty();
        self.port.emit('select-issue');
    });
}

function buildIssueLinkElement(linkItem) {
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
        "<a class= \"issue-link\" href=\"#\">" + linkItem.key + linkTypeSpan + "</a>" +
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
    return "<a class=\"list-group-item\">" +
        "<p class=\"list-group-item-heading\">" + annotation.comment + "</p>" +
        "    <blockquote class=\"list-group-item-text\">" +
        annotation.anchorText +
        "</blockquote>" +
        "</a>";
}

function setGeneralFields(issue) {
    $("a#issueLink").attr("href", issue.self);
    $("#status").text(issue.status);
    $("#type").text(issue.type);
    $("#summary").text(issue.summary);
}

console.log("Script is loaded");