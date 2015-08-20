(function (window, document, undefined) {
    console.log("session panel initialized...");
    self.port.on("loadResearches", function (researches) {
        console.log(researches);
        var researchElement = jQuery("#researches");
        for (var inx in researches) {
            if (researches.hasOwnProperty(inx)) {
                var research = researches[inx];
                var rTemplate = researchTemplate(research);
                researchElement.append(rTemplate);
                jQuery(rTemplate).data("research", research);
                jQuery(rTemplate).click(function () {
                    self.port.emit("startResearchSession", jQuery(this).data("research").key);
                });
            }
        }
    });

    function researchTemplate(research) {
        return jQuery.parseHTML("<li class=\"list-group-item\">" +
            "<a class= 'issue-link key' href='#'>" + research.key + "</a>" +
            "<span>" + research.fields.summary + "</span>" +
            "</li>");
    }
})(window, document);