(function (window, document, undefined) {
    addon.port.on("loadResearchSessions", function (researches) {
        console.log("Sidebar loading research sessions' annotations...");
        console.log(researches);
        var researchesElement = jQuery(".panel.list-group");
        researchesElement.empty();
        for (var research in researches) {
            if (researches.hasOwnProperty(research)) {
                var rTemplate = researchTemplate(research);
                researchesElement.append(rTemplate);

                var annotationsField = rTemplate.find("div.list-group.list-annotations");
                var sessions = researches[research].sessions;
                for (var session in sessions) {
                    if (sessions.hasOwnProperty(session)) {
                        var temp = sessions[session];

                        var sTemplate = sessionTemplate(temp);
                        jQuery(sTemplate).data("annotation", temp);
                        $(sTemplate).click(function () {
                            addon.port.emit("openAnnotationLink", $(this).data("annotation"));
                        });
                        annotationsField.append(sTemplate);
                    }
                }

            }
        }
    });

    function researchTemplate(research) {
        return jQuery("<div/>").html("<a class=\"list-group-item\" data-toggle=\"collapse\" data-target=\"#" + research + "\" data-parent=\"#phases\">" +
            research +
            "</a>" +
            "<div id='" + research + "' class=\"sublinks collapse\">" +
            "<div class=\"panel voffset2\">" +
            "<div class=\"panel-body fixed-panel\">" +
            "<div class=\"list-group list-annotations\">" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>").contents();
    }

    function sessionTemplate(session) {
        return jQuery.parseHTML("<a class=\"list-group-item\">\
        <blockquote class=\"list-group-item-text annotation-comment\">" +
            session.quote +
            "</blockquote>\
            <div class=\"annotation-comment\">\
            <p class=\"list-group-item-heading\">" + session.text + "</p>\
            </div>\
            </a>");
    }
})(window, document);