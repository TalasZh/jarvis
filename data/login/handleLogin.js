var loginButton = $("#loginButton");
var issueLinks;

if (loginButton !== null) {
    loginButton.click(function (event) {
        var username = $("#username");
        var password = $("#password");
        console.log("Hello " + username.text());
        self.port.emit("handle-login", username.value, password.value);
        username.text('');
        password.text('');
    });
}

var annotator = $("#annotator");
if (annotator !== null) {
    annotator.click(function (event) {

        console.log(annotator.className);
        if (annotator.prop("class") === "btn btn-primary btn-sm") {
            annotator.prop("class", "btn btn-default btn-sm");
        }
        else {
            //Enable annotator
            annotator.prop("class", "btn btn-primary btn-sm");
        }

        if (event.button == 0 && event.shiftKey == false) {
            self.port.emit('left-click');
        }

        if (event.button == 2 || (event.button == 0 && event.shiftKey == true)) {
            self.port.emit('right-click');
            console.log("right-click");
            event.preventDefault();
        }
    });
}

$("a.projects").click(function () {
    self.port.emit('handle-login');
});

var selectProjectBtn = $("#selectProject");
if (selectProjectBtn !== null) {
    selectProjectBtn.click(function () {
        var x = $("#selectProjectCombobox[name='selectProjectCombobox']");
        var selectValue = x.find("option:selected").text();
        self.port.emit("project-selected", selectValue);

    });
}

var selectIssue = $("#selectIssue");
if (selectIssue !== null) {
    selectIssue.click(function () {
        var x = $("#issueCombobox");
        var selectValue = x.find("option:selected").text();
        self.port.emit("issue-selected", selectValue);
    });
}

var backButton = $("#backButton");
if (backButton !== null) {
    backButton.click(function (event) {
        var x = $("#issueNumber").html();
        var y = x.substr(0, x.indexOf('-'));
        self.port.emit("back-button-pressed", y);
    });
}

var backButtonOnResearchPage = $("#backButtonOnResearchPage");
if (backButtonOnResearchPage !== null) {
    backButtonOnResearchPage.click(function (event) {
        self.port.emit("back-button-pressed-on-researchpage");
    });
}

var backButtonOnProjectSelectionPage = $("#backButtonOnProjectSelectionPage");
if (backButtonOnProjectSelectionPage !== null) {
    backButtonOnProjectSelectionPage.click(function (event) {
        self.port.emit("back-button-pressed-on-project-selection-page");
    });
}


self.port.on("fill-combo-box", function (json, projectKey) {
    console.log("Fill combo box");
    console.log(projectKey);
    //fillComboBox(json);
    pushProjectIssues(json);
    $("#project-link").text(projectKey);
});

self.port.on("fill-project-combobox", function (json, projectKey) {
    fillProjectCombobox(json, projectKey);
});

self.port.on("update-project-information", function (json) {
    updateProjectInfo(json);
});

function getDateTime() {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    if (month.toString().length == 1) {
        var month = '0' + month;
    }
    if (day.toString().length == 1) {
        var day = '0' + day;
    }
    if (hour.toString().length == 1) {
        var hour = '0' + hour;
    }
    if (minute.toString().length == 1) {
        var minute = '0' + minute;
    }
    if (second.toString().length == 1) {
        var second = '0' + second;
    }
    var dateTime = year + '/' + month + '/' + day + ' ' + hour + ':' + minute + ':' + second;
    return dateTime;
}

// fill out combo box options
function fillComboBox(json) {
    var x = $("#issueCombobox");
    x.change(function (event) {
        var selectValue = x.find("option:selected").text();
        console.log(selectValue + " : " + JSON.stringify(json));
    });

    console.log(json.size);
    for (var i = 0; i < json.length; i++) {
        var issue = json[i];
        x.append($("<option></option>").text(issue.key));
    }
    x.find('option:eq(0)').attr('selected', true);
    x.trigger("change");
}

// fill out projects combo box options
function fillProjectCombobox(json, projectKey) {
    console.log("Fill Project Combobox");
    var x = $("#selectProjectCombobox");
    x.change(function (event) {
        var selectValue = x.find("option:selected").text();

        console.log(selectValue);
        self.port.emit("project-changed", selectValue);
    });

    for (var i = 0; i < json.length; i++) {
        var project = json[i];
        x.append($("<option></option>").text(project.key));
    }
    if (projectKey) {
        x.find('option:contains(' + projectKey + ')').attr('selected', true);
    }
    else {
        x.find('option:eq(0)').attr('selected', true);
    }

    x.trigger("change");
}

function updateProjectInfo(json) {
    $("#name").html(json.name);
    $("#key").html(json.key);
    $("#description").html(json.description);
    $("#versions").html(json.versions);
}


var issueLink = $("#issueLink");
if (issueLink !== null) {
    issueLink.click(function (event) {
        self.port.emit("link-clicked", issueLink.text());
    });
}

function pushProjectIssues(links) {
    let issueList = $("#project-list-issues");
    issueList.empty();
    for (let item of links) {
        issueList.append(buildIssueLinkElement(item));
    }
    //notifies controller to select another issue
    $("a.issue-link").click(function () {
        console.log("Issue Clicked: " + $(this).text().trim());
        self.port.emit('issue-selected',
            $(this).text().trim()
        );
    });

    //for sorting and searching features
    var options = {valueNames: ["key", "issueType", "projectKey"]};
    issueLinks = new List("project-issues", options);
    issueLinks.sort("issueType", {order: "asc"});

    $("#sortByKey").click(function () {
        var span = $(this).find("span");
        if (span.prop("class") ===
            "glyphicon glyphicon-sort-by-alphabet") {
            issueLinks.sort("key", {order: "asc"});
            span.prop("class", "glyphicon glyphicon-sort-by-alphabet-alt");
        }
        else {
            issueLinks.sort("key", {order: "desc"});
            span.prop("class", "glyphicon glyphicon-sort-by-alphabet");
        }
    });

    $("#sortByType").click(function () {
        var span = $(this).find("span");
        if (span.prop("class") ===
            "glyphicon glyphicon-sort-by-alphabet") {
            issueLinks.sort("issueType", {order: "asc"});
            span.prop("class", "glyphicon glyphicon-sort-by-alphabet-alt");
        }
        else {
            issueLinks.sort("issueType", {order: "desc"});
            span.prop("class", "glyphicon glyphicon-sort-by-alphabet");
        }
    });

    $("#searchProjectIssues").keyup(function () {
        var criteria = $(this).val();
        console.log("Searching for criteria: " + criteria);
        if (criteria) {
            issueLinks.search(criteria);
        }
        else {
            issueLinks.search();
        }
    });
}