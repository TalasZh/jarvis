/**
 * handles new issue and substitutes all required fields
 */
self.port.on('set-issue', function (issue) {
    if (!issue) {
        return;
    }
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
        $(this).find('span').remove();
        self.port.emit('select-issue',
            $(this).text().trim()
        );
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
    $("#issueNumber").text(issue.key);
    buildCrumbs(issue);
}

function buildCrumbs(issue) {
    let issuePath = $("#issue-path");
    issuePath.empty();

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
        if (link.type === "Story" && link.key !== issue.key) {
            issuePath.append(breadcrumbItemBuilder(link.key, false, "issue"));
            break;
        }
    }
    for (let link of issue.links) {
        if (link.type === "Phase" && link.key !== issue.key) {
            issuePath.append(breadcrumbItemBuilder(link.key, false, "issue"));
            break;
        }
    }
    issuePath.append(breadcrumbItemBuilder(issue.key, true));
    issuePath.find("a").click(function () {
        self.port.emit('select-issue',
            $(this).text().trim()
        );
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