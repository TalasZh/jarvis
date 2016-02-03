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

$(window).scroll(function() {
    if($(window).scrollTop() > 0 ) {
       self.port.emit("page-scrooled");
    } 
});

body.mouseup(function (e){
    var scroolSize = 0;
    if($(window).scrollTop() > 0 ) {
       scroolSize = $(window).scrollTop();
    }
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
            (e.pageY - scroolSize)
        );
        scrollSize = 0;
    }
});