self.on('message', function (message) {
    $('#annotation').text(message);
    // $('#annotation').text(message);
  // $("#annotation").val(message);

  // console.log( "insidde annotation.js ");

  // $('#annotation').data('markdown').setContent(message);
  var post = $('#annotation').data('markdown').parseContent();
  var post1 = $('#annotation').data('markdown').getContent();

  

  console.log( post );
  console.log( post1 );
  // $('#annotation').data('markdown').setContent(post);
  $('#annotation').data('markdown').showPreview();

});