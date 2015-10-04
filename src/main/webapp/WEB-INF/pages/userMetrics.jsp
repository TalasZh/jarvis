<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title></title>

  <!--Stylesheets-->
  <link href="css/libs/bootstrap.min.css" rel="stylesheet">
  <link href="css/font-awesome.min.css" rel="stylesheet">

  <link href="css/style.css" rel="stylesheet">
  <link href="css/components.css" rel="stylesheet">
  <style>
    html, body
    {
      height: 100%;
      width: 100%;
    }

    canvas
    {
      display: block;
    }
  </style>
</head>
<body ng-app="jarvis">

<div ui-view></div>

<script src="js/libs/angular.min.js"></script>
<script src="js/libs/angular-ui-router.min.js"></script>
<script src="js/libs/angular-animate.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/ngStorage/0.3.9/ngStorage.min.js"></script>
<script src="js/libs/ocLazyLoad.min.js"></script>
<script src="js/libs/ui-bootstrap-tpls-0.13.4.min.js"></script>

<script src="js/angular/jarvis.js"></script>

<script src="js/libs/jquery-2.1.4.min.js"></script>
<script src="js/libs/bootstrap.min.js"></script>

<script src="js/leap-js/leap-0.6.4.min.js"></script>
<script src="js/leap-js/leap-plugins-0.1.6.js"></script>

<script src="js/libs/addons/date-format.js"></script>
<script src="js/libs/sprintf.js"></script>

<script src="js/angular/timeline/controller.js"></script>


<!--Knob charts-->
<script src="js/libs/addons/jquery.knob.min.js"></script>

<!--[if IE]>
<script type="text/javascript" src="js/excanvas.js"></script>
<![endif]-->

</body>
</html>
