<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ermek
  Date: 7/27/15
  Time: 4:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><fmt:message key="userMetrics.title"/></title>

  <!-- Loading Flat UI -->
  <link href="../../styles/flat-ui.css" rel="stylesheet">

  <link rel="stylesheet" type="text/css" href="../../styles/css/style1.css"/>
  <script src="../../scripts/modernizr.custom.63321.js"></script>


  <style>
    canvas
    {
    }
  </style>

</head>


<body class="body-bg">

<h2><fmt:message key="userMetrics.heading"/></h2>


<div class="row">
  <div class="col-md-4">

    <div class="panel panel-default">
      <div class="panel-heading">Projects</div>

      <div class="panel-body">
        <hr>

        <div>
          <label for="switch">User</label>
          <input type="checkbox" checked data-toggle="toggle" id="switch">
        </div>

        <div class="fleft">
          <select id="cd-dropdown" name="cd-dropdown" class="cd-select">
            <option value="-1" selected>Choose Project</option>
            <option value="1" class="icon-rocket">Subutai</option>
            <option value="2" class="icon-diamond">Jarvis</option>
            <option value="3" class="icon-star">HUB</option>
            <option value="4" class="icon-sun">Tooling</option>
          </select>
        </div>

        <s:submit type="button" key="button.upload" name="list" cssClass="btn btn-primary" theme="simple">
          <i class="icon-upload icon-white"></i>
          <fmt:message key="button.view"/>
        </s:submit>

      </div>
    </div>
  </div>

  <div class="col-md-4">

    <div class="panel panel-default">
      <div class="panel-heading">Developers</div>
      <div class="panel-body">

        <hr>

        <div class="list-group">
          <a href="#" class="list-group-item ">
            Eric
            <span class="badge">5</span>
          </a>
          <a href="#" class="list-group-item">
            Talas <span class="badge">9</span>
          </a>
          <a href="#" class="list-group-item">
            Neslihan <span class="badge">6</span>
          </a>
          <a href="#" class="list-group-item">
            Kazim <span class="badge">3</span>
          </a>
        </div>
      </div>
    </div>
  </div>


  <div class="col-md-4">

    <div class="panel panel-default">
      <div class="panel-heading">Metrics</div>
      <div class="panel-body">

        <hr>

        <div style="width:100%">
          <canvas id="canvas" height="100" width="100"></canvas>
        </div>

      </div>
    </div>
  </div>


</div>

<script>
  var radarChartData = {
    labels: ["Productivity", "Collaboration", "Velocity"],
    datasets: [
      {
        label: "My Second dataset",
        fillColor: "rgba(151,187,205,0.2)",
        strokeColor: "rgba(151,187,205,1)",
        pointColor: "rgba(151,187,205,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(151,187,205,1)",
        data: [28, 48, 40]
      }
    ]
  };

  window.onload = function () {
    window.myRadar = new Chart(document.getElementById("canvas").getContext("2d")).Radar(radarChartData, {
      responsive: true
    });
  }

</script>


<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="../../scripts/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="../../scripts/flat-ui.min.js"></script>

<script src="../../scripts/application.js"></script>

<script src="../../scripts/Chart.js"></script>
<link href="https://gitcdn.github.io/bootstrap-toggle/2.2.0/css/bootstrap-toggle.min.css" rel="stylesheet">
<script src="https://gitcdn.github.io/bootstrap-toggle/2.2.0/js/bootstrap-toggle.min.js"></script>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript" src="../../scripts/jquery.dropdown.js"></script>
<script type="text/javascript">

  $(function () {

    $('#cd-dropdown').dropdown({
      gutter: 5
    });

  });

</script>

</body>
</html>
