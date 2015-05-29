self.on('message', function onMessage(annotations) {
    // annotations.forEach(
    //     function (annotation) {
    //         if (annotation.url == document.location.toString()) {
    //             createAnchor(annotation);
    //         }
    //     });

    // let annotated = $('.annotated');
    // annotated.css('border', 'solid 3px yellow');

    // annotated.bind('mouseenter', function (event) {
    //     self.port.emit('show', $(this).attr('annotation'));
    //     event.stopPropagation();
    //     event.preventDefault();
    // });

    // annotated.bind('mouseleave', function () {
    //     self.port.emit('hide');
    // });

    $('<style type="text/css">.highlight { background-color: #FFFF88; }</style>').appendTo($('head'));
    console.log("highlighting ...");
    var sss = "Keep reading to learn how";
    $("body").highlight(sss);
});

function createAnchor(annotation) {
    annotationAnchorAncestor = $('#' + annotation.ancestorId);
    annotationAnchor = $(annotationAnchorAncestor).parent().find(
        ':contains(' + annotation.anchorText + ')').last();
    $(annotationAnchor).addClass('annotated');
    $(annotationAnchor).attr('annotation', annotation.annotationText);
}