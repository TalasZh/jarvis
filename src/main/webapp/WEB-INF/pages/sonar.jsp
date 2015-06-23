<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="sonar.title"/></title>
  <meta name="menu" content="Sonar"/>


  <!--Load the AJAX API-->
  <script type="text/javascript" src="https://www.google.com/jsapi"></script>
  <script type="text/javascript">

    // Load the Visualization API and the piechart package.
    google.load('visualization', '1.0', {'packages': ['corechart']});

    // Set a callback to run when the Google Visualization API is loaded.
    google.setOnLoadCallback(drawChart);

    // Callback that creates and populates a data table,
    // instantiates the pie chart, passes in the data and
    // draws it.
    function drawChart() {

      // Create the data table.
      var data = new google.visualization.DataTable();
      data.addColumn('string', 'Topping');
      data.addColumn('number', 'Slices');
      data.addRows([
        ['Mushrooms', 3],
        ['Onions', 1],
        ['Olives', 1],
        ['Zucchini', 1],
        ['Pepperoni', 2]
      ]);

      // Set chart options
      var options = {
        'title': 'How Much Pizza I Ate Last Night',
        'width': 400,
        'height': 300
      };

      // Instantiate and draw our chart, passing in some options.
      var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
      chart.draw(data, options);
    }
  </script>


</head>
<body class="home">

<h2><fmt:message key="sonar.heading"/></h2>

<p><fmt:message key="sonar.message"/></p>


<div class="container-fluid">

  <div class="row">
    <div class="col-sm-7">
      <s:form action="sonar" enctype="application/x-www-form-urlencoded" method="post" validate="true" id="sonarForm"
              cssClass="well">

        <s:select label="Project"
                  headerKey="-1" headerValue="--- Select ---"
                  list="resources"
                  listKey="key"
                  listValue="name"
                  name="project"/>

        <div id="actions" class="form-group">
          <s:submit type="button" key="button.upload" name="list" cssClass="btn btn-primary" theme="simple">
            <i class="icon-upload icon-white"></i>
            <fmt:message key="button.view"/>
          </s:submit>

          <a class="btn btn-default" href="home">
            <i class="icon-remove"></i>
            <fmt:message key="button.cancel"/>
          </a>
        </div>
      </s:form>
    </div>

  </div>

  <c:if test="${complexityStats != null}">

    <div class="row">
      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Complexity</div>
          <div class="panel-body">

            <ul class="list-group">
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{complexityStats.classComplexity})"/></span>
                Class
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{complexityStats.functionComplexity})"/></span>
                Function
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{complexityStats.fileComplexity})"/></span>
                File
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{complexityStats.complexity})"/></span>
                Total
              </li>
            </ul>

          </div>
        </div>
      </div>

      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Duplications</div>
          <div class="panel-body">

            <ul class="list-group">
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{duplicationStats.duplicationPercent})"/></span>
                Duplication Percent
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{duplicationStats.duplicatedLines})"/></span>
                Duplicated Lines
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{duplicationStats.duplicatedBlocks})"/></span>
                Duplicated Blocks
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{duplicationStats.duplicatedFiles})"/></span>
                Duplicated Files
              </li>
            </ul>

          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-4">
        <div class="panel panel-default">
          <div class="panel-heading">Quantitative Stats</div>
          <div class="panel-body">

            <ul class="list-group">
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{quantitativeStats.linesOfCode})"/></span>
                Lines Of Code
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{quantitativeStats.lines})"/></span>
                Lines
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{quantitativeStats.files})"/></span>
                Files
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{quantitativeStats.directories})"/></span>
                Directories
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{quantitativeStats.functions})"/></span>
                Functions
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.classes})"/></span>
                Classes
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{quantitativeStats.statements})"/></span>
                Statements
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{quantitativeStats.accessors})"/></span>
                Accessors
              </li>
            </ul>

          </div>
        </div>
      </div>


      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Unit Tests</div>
          <div class="panel-body">
            <ul class="list-group">
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{unitTestStats.successPercent})"/></span>
                Success Percent
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{unitTestStats.failures})"/></span>
                Failures
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{unitTestStats.errors})"/></span>
                Errors
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{unitTestStats.testsCount})"/></span>
                Tests Count
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{unitTestStats.executionTime})"/></span>
                Execution Time(sec)
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{unitTestStats.coveragePercent})"/></span>
                Coverage Percent
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{unitTestStats.lineCoveragePercent})"/></span>
                Line Coverage Percent
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{unitTestStats.branchCoveragePercent})"/></span>
                Branch Coverage Percent
              </li>
            </ul>

          </div>
        </div>

      </div>
      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Violation Stats</div>
          <div class="panel-body">
            <ul class="list-group">
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.float',{violationStats.technicalDebt})"/></span>
                Technical Debt(days)
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property
                    value="getText('number.format.int',{violationStats.openIssues})"/></span>
                Open Issues
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{violationStats.reopenedIssues})"/></span>
                Reopened Issues
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property value="getText('number.format.int',{violationStats.allIssues})"/></span>
                All Issues
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{violationStats.blockerIssues})"/></span>
                Blocker Issues
              </li>
              <li class="list-group-item">
              <span class="badge"><s:property
                  value="getText('number.format.int',{violationStats.criticalIssues})"/></span>
                Critical Issues
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property
                    value="getText('number.format.int',{violationStats.majorIssues})"/></span>
                Major Issues
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property
                    value="getText('number.format.int',{violationStats.minorIssues})"/></span>
                Minor Issues
              </li>
              <li class="list-group-item">
                <span class="badge"><s:property
                    value="getText('number.format.int',{violationStats.infoIssues})"/></span>
                Info Issues
              </li>
            </ul>

          </div>
        </div>

      </div>

    </div>


  </c:if>
</div>


<%--<s:property value="resources"/>--%>
<!--Div that will hold the pie chart-->
<%--<div id="chart_div"></div>--%>

</body>