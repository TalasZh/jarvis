self.on('message', function onMessage(annotations) {
    for (var key in annotations) {
        if (annotations.hasOwnProperty(key)) {
            let annotation = annotations[key];
            if (annotation.url == document.location.toString()) {
                createAnchor(annotation);
            }
        }
    }

    let annotated = $('.annotated');
    annotated.css('border', 'solid 3px yellow');

    annotated.bind('mouseenter', function (event) {
        self.port.emit('show', $(this).attr('annotation'));
        event.stopPropagation();
        event.preventDefault();
    });

    annotated.bind('mouseleave', function () {
        self.port.emit('hide');
    });
});

function createAnchor(annotation) {
    annotationAnchorAncestor = $('#' + annotation.ancestorId);
    annotationAnchor = $(annotationAnchorAncestor).parent().find(
        ':contains(' + annotation.anchorText + ')').last();
    $(annotationAnchor).addClass('annotated');
    $(annotationAnchor).attr('annotation', annotation.comment);
}