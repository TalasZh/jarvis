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
        self.port.emit("show-popup", relX, relY );
    } 
});





// all.mouseenter(function () {
//     if (!active || $(this).hasClass('annotated')) {
//         return;
//     }
//     resetMatchedElement();
//     ancestor = $(this).closest("[id]");
//     matchedElement = $(this).first();
//     originalBgColor = $(matchedElement).css('background-color');
//     $(matchedElement).css('background-color', 'yellow');
//     $(matchedElement).bind('click.annotator', function (event) {
//         event.stopPropagation();
//         event.preventDefault();
//         self.port.emit('show',
//             [
//                 document.location.toString(),
//                 $(ancestor).attr("id"),
//                 $(matchedElement).text()
//             ]
//         );
//     });
// });

// all.mouseout(function () {
//     resetMatchedElement();
// });