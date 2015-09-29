<html>
<head>

  <!--<%-- 3d timeline styles --%>-->
  <link href="../../styles/timeline/css/bootstrap.min.css" rel="stylesheet">
  <link href="../../styles/timeline/css/style.css" rel="stylesheet">
  <link href="../../styles/timeline/css/font-awesome.min.css" rel="stylesheet">
  <link href="../../styles/timeline/css/animations.css" rel="stylesheet">
  <link href="../../styles/timeline/css/daterangepicker.css" rel="stylesheet"/>

</head>


<body>

<!--||||||SLIDER PROJECT FILTER BEGIN||||||-->
<div class="slide-out-div">


  <a class="handle" href="#"></a>

  <div id="filter-by"></div>

  <div class="btn-group">
    <label for="project-dropdown">PROJECT
      <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="project-dropdown">
        Select project<span class="caret" style="margin-left: 80px;"></span></button>
      <ul class="dropdown-menu" role="menu">
        <li><a href="#">Jarvis</a></li>
        <li><a href="#">Tulpar</a></li>
      </ul>
    </label>
  </div>


  <div class="btn-group" style="margin-top: 20px">
    <label for="member-dropdown" id="team-member"><span id="team-member-span">TEAM MEMBERS</span>

      <div id="content">
        <input type="checkbox" class="check" id="one" name="check_one" checked/>
        <label for="one">
          <div id="thumb"></div>
        </label>
      </div>

      <select class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="member-dropdown">
        <option value="0">Select member</option>
      </select>
    </label>
  </div>

  <div class="btn-group" data-toggle="buttons" id="btn-group-slider">
    <label for="radio-slider-id">EPIC STATUS &nbsp;&nbsp;&nbsp;
      <label class="radio-slider" id="radio-slider-id">
        <input type="radio" name="" value="1"/>&nbsp;&nbsp;Open&nbsp;&nbsp;&nbsp;
      </label>
      <label class="radio-slider">
        <input type="radio" name="" value="2"/>&nbsp;&nbsp;Closed
      </label>
    </label>
  </div>


  <div class="text-center" style="margin-top: 20px">
    <div class="btn btn-default" id="btn-apply">APPLY</div>
  </div>

</div>
<!--||||||SLIDER PROJECT FILTER END||||||-->



<!--||||||POP-UP MODAL BEGIN||||||-->
<div id="popup">
  <div id="popup-part"></div>
  <div id="popup-carrot"></div>
  <div class="text-center popup-contents">
    <div class="blocker">Blocker</div>
    <h1>#152</h1><span class="popup-contents-name">Nataly Ivanova</span>
    <hr/><span class="popup-contents-finish">Did not finish <br/>#54</span>
  </div>
</div>
<!--||||||POP-UP MODAL END||||||-->

<div id="position" style="position:fixed;background: blue; width: 5px; height: 5px;"></div>

<canvas id="renderCanvas"></canvas>

<!--<%-- 3d timeline scripts --%>-->
<script src="../../styles/timeline/js/libs/date-format.js"></script>
<script src="../../styles/timeline/js/libs/jquery-2.1.4.min.js"></script>
<script src="../../styles/timeline/js/libs/bootstrap.min.js"></script>--%>


<script src="../../styles/timeline/js/libs/moment.min.js"></script>
<script src="../../styles/timeline/js/libs/daterangepicker.js"></script>
<script src="../../styles/timeline/js/libs/jquery.tabSlideOut.v1.3.js"></script>

<!--SlideBar-->
<script type="text/javascript">
  $(function () {
    $('.slide-out-div').tabSlideOut({
      tabHandle: '.handle',                     //class of the element that will become your tab
      pathToTabImage: '../../styles/timeline/assets/img/slidebar-tag.svg', //path to the image for the tab //Optionally can be set using css
      imageHeight: '100px',                     //height of tab image           //Optionally can be set using css
      imageWidth: '20px',                       //width of tab image            //Optionally can be set using css
      tabLocation: 'left',                      //side of screen where tab lives, top, right, bottom, or left
      speed: 300,                               //speed of animation
      action: 'click',                          //options: 'click' or 'hover', action to trigger animation
      topPos: '400px',                          //position from the top/ use if tabLocation is left or right
      leftPos: '15px',                          //position from left/ use if tabLocation is bottom or top
      fixedPosition: false                      //options: true makes it stick(fixed position) on scroll
    });

  });

  $(function () {
    $('input[name="daterange"]').daterangepicker();
  });
</script>

<!--<script src="js/leapCursor/leapcursor-with-dependencies.min.js"></script>-->

<!--<script src="js/babylon/babylon.max.js"></script>-->
<script src="../../styles/timeline/js/babylon/babylon.2.1.debug.js"></script>
<script src="../../styles/timeline/js/libs/leap-0.6.4.min.js"></script>
<script src="../../styles/timeline/js/libs/leap-plugins-0.1.6.js"></script>

<script src="../../styles/timeline/js/builder-timeline.js"></script>
<!--<script src="js/app.js"></script>-->

<script src="../../styles/timeline/js/newApp.js"></script>


</body>
</html>
