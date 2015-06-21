self.on('message', function (message) {
    var md = $("#comment-md").data('markdown');
    md.hideButtons('all');
    md.setContent(message);
    md.showPreview();
});

self.port.on("hidePreview", function(){
  var md = $("#comment-md").data('markdown');
  md.hidePreview();
})


let html = $('html');
html.mouseleave(function() {
  self.port.emit("mouseout-event");
});