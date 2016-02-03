$("#menu-toggle").click(function (e) {
    e.preventDefault();
    $("#wrapper-left").toggleClass("active-left");
    $(".profile").toggleClass("active-profile");
    $(".circle-avatar").toggleClass("active-avatar");
    $(".icons-service").toggleClass("active-icon");
    $(".show-text").toggleClass("hide-text");
    $(".si-icon").toggleClass("si-active");
    $(".canvas-conf").toggleClass("canvas-l-active");
    $(".icon-collapse").toggleClass("rotate180");

    var val = -100;
    if ($("#wrapper-left.active-left").length > 0) {
        val = 100;
    }
    $("#viewport").attr('width', parseInt($("#viewport").attr('width')) + val);
});
$(".menu-toggle-right").click(function (e) {
    e.preventDefault();
    $(".wrapper-right").toggleClass("active-right");
    $(".right-nav").toggleClass("active-rnav");
    $(".si-wide").toggleClass("short");
    $(".ng-isolate-scope").toggleClass("hide");
    $(".hide-icon").toggleClass("show");
    $(".icon-collapse-right").toggleClass("hide");
    $(".toggle-right a").toggleClass("toggle-active");

    var val = -301;
    if ($(".wrapper-right.active-right").length > 0) {
        val = 301;
    }

    $("#viewport").attr('width', parseInt($("#viewport").attr('width')) + val);
});
