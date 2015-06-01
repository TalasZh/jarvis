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

function getSelectionText() {
    var text = "";
    if (window.getSelection) {
        text = window.getSelection().toString();
    } else if (document.selection && document.selection.type != "Control") {
        text = document.selection.createRange().text;
    }
    return text;
}

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
