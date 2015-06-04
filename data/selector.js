var matchedElement = null;
var originalBgColor = null;
var active = false;

function resetMatchedElement() {
    if (matchedElement) {
        (matchedElement).css('background-color', originalBgColor);
        (matchedElement).unbind('click.annotator');
    }
}

self.on('message', function onMessage(activation) {
    active = activation;
    if (!active) {
        resetMatchedElement();
    }
});

let all = $('*');
let body = $('body');


function getSelectionText() {
    var text = "";
    if (window.getSelection) {
        text = window.getSelection().toString();
    } else if (document.selection && document.selection.type != "Control") {
        text = document.selection.createRange().text;
    }
    return text;
}


body.mouseup(function (e){
    var parentOffset = $(this).parent().offset(); 
    var relX = e.pageX - parentOffset.left;
    var relY = e.pageY - parentOffset.top;
    if ( getSelectionText().length > 0 ){
        console.log(getSelectionText());

        self.port.emit(
            "show-popup",
            [
                document.location.toString(),
                "content",
                getSelectionText()
            ],
            e.pageX,
            e.pageY
        );
    }
});