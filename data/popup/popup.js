var annotateButton = $("#annotate");
if (annotateButton !== null) {
    annotateButton.click(function (event) {
        self.port.emit("annotate-button-pressed");
    });
}

var highlightButton = $("#highlight");
if (highlightButton !== null) {
    highlightButton.click(function (event) {
	    self.port.emit("highlight-button-pressed");
    });
}

self.port.on('highlight', function(annotation) {
	console.log( annotation );
    $('<style type="text/css">.highlight { background-color: #FFFF88; }</style>').appendTo($('head'));
    $("body").highlight(annotation.anchorText);
});

