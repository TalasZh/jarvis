var annotateButton = $("#annotate");
if (annotateButton !== null) {
    annotateButton.click(function (event) {
        console.log( "buton clicked ");
        self.port.emit("annotate-button-pressed");
    });
}
