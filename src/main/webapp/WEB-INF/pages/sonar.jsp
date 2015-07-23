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
<body class="body-bg">


<h2><fmt:message key="sonar.heading"/></h2>

<p><fmt:message key="sonar.message"/></p>


<div class="container-fluid">

  <div class="row">
    <div class="col-sm-4">
      <div class="panel panel-default">
        <div align="center" style="padding: 15px 15px">
          <s:form action="sonar" enctype="application/x-www-form-urlencoded" method="post" validate="true"
                  id="sonarForm"
                  cssClass="">

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

    </div>
  </div>

  <c:if test="${complexityStats != null}">

    <div class="row">
      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Complexity</div>
          <div class="panel-body">

            <table class="table table-striped table-bordered table-hover" cellpadding="2">
              <tr>
                <td>Class</td>
                <td class="col-md-2" align="right"><span class="label label-warning label-as-badge"><s:property
                    value="getText('number.format.float',{complexityStats.classComplexity})"/></span></td>
              </tr>
              <tr>
                <td>Function</td>
                <td align="right"><span class="label label-warning label-as-badge"><s:property
                    value="getText('number.format.float',{complexityStats.functionComplexity})"/></span></td>
              </tr>
              <tr>
                <td>File</td>
                <td align="right"><span class="label label-warning label-as-badge"><s:property
                    value="getText('number.format.float',{complexityStats.fileComplexity})"/></span></td>
              </tr>
              <tr>
                <td>Total</td>
                <td align="right"><span class="label label-warning label-as-badge"><s:property
                    value="getText('number.format.float',{complexityStats.complexity})"/></span></td>
              </tr>
            </table>

          </div>
        </div>
      </div>

      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">Duplications</div>
          <div class="panel-body">

            <table class="table table-striped table-bordered table-hover" cellpadding="2">
              <tr>
                <td>Dublication Percent</td>
                <td class="col-md-2" align="right"><span class="label label-primary label-as-badge"><s:property
                    value="getText('number.format.float',{duplicationStats.duplicationPercent})"/></span></td>
              </tr>
              <tr>
                <td>Dublicated Lines</td>
                <td align="right"><span class="label label-primary label-as-badge"><s:property
                    value="getText('number.format.int',{duplicationStats.duplicatedLines})"/></span></td>
              </tr>
              <tr>
                <td>Dublicated Blocks</td>
                <td align="right"><span class="label label-primary label-as-badge"><s:property
                    value="getText('number.format.int',{duplicationStats.duplicatedBlocks})"/></span></td>
              </tr>
              <tr>
                <td>Duplicated Files</td>
                <td align="right"><span class="label label-primary label-as-badge"><s:property
                    value="getText('number.format.int',{duplicationStats.duplicatedFiles})"/></span></td>
              </tr>
            </table>

          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-4">
        <div class="panel panel-default">
          <div class="panel-heading">
            <strong> Quantitative Stats </strong>
          </div>
          <div class="panel-body">

            <table class="table table-striped table-bordered table-hover" cellpadding="2">
              <tr>
                <td>Lines Of Code</td>
                <td class="col-md-2" align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.linesOfCode})"/></span></td>
              </tr>
              <tr>
                <td>Lines</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.lines})"/></span></td>
              </tr>
              <tr>
                <td>Files</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.files})"/></span></td>
              </tr>
              <tr>
                <td>Directories</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.directories})"/></span></td>
              </tr>
              <tr>
                <td>Functions</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.functions})"/></span></td>
              </tr>
              <tr>
                <td>Classes</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.classes})"/></span></td>
              </tr>
              <tr>
                <td>Statements</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.statements})"/></span></td>
              </tr>
              <tr>
                <td>Accessors</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{quantitativeStats.accessors})"/></span></td>
              </tr>
            </table>


          </div>
        </div>
      </div>


      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">
            <strong> Unit Tests </strong>
          </div>
          <div class="panel-body">

            <table class="table table-striped table-bordered table-hover" cellpadding="2">
              <tr>
                <td>Success Percent</td>
                <td align="right" class="col-md-2"><span class="label label-success label-as-badge"><s:property
                    value="getText('number.format.float',{unitTestStats.successPercent})"/></span></td>
              </tr>
              <tr>
                <td>Failures</td>
                <td align="right"><span class="label label-danger label-as-badge"><s:property
                    value="getText('number.format.int',{unitTestStats.failures})"/></span></td>
              </tr>
              <tr>
                <td>Errors</td>
                <td align="right"><span class="label label-danger label-as-badge"><s:property
                    value="getText('number.format.int',{unitTestStats.errors})"/></span></td>
              </tr>
              <tr>
                <td>Test Count</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{unitTestStats.testsCount})"/></span></td>
              </tr>
              <tr>
                <td>Execution Time(sec)</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.float',{unitTestStats.executionTime})"/></span></td>
              </tr>
              <tr>
                <td>Coverage Percent</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.float',{unitTestStats.coveragePercent})"/></span></td>
              </tr>
              <tr>
                <td>Line Coverage Percent</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.float',{unitTestStats.lineCoveragePercent})"/></span></td>
              </tr>
              <tr>
                <td>Branch Coverage Percent</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.float',{unitTestStats.branchCoveragePercent})"/></span></td>
              </tr>
            </table>

          </div>
        </div>

      </div>
      <div class="col-md-4">

        <div class="panel panel-default">
          <div class="panel-heading">
            <strong> Violation Stats </strong>
          </div>
          <div class="panel-body">

            <table class="table table-striped table-bordered table-hover" cellpadding="2">
              <tr>
                <td>Technical Debt(days)</td>
                <td align="right" class="col-md-2"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.float',{violationStats.technicalDebt})"/></span></td>
              </tr>
              <tr>
                <td>Open Issues</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.openIssues})"/></span></td>
              </tr>
              <tr>
                <td>Reopened Issues</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.reopenedIssues})"/></span></td>
              </tr>
              <tr>
                <td>All Issues</td>
                <td align="right"><span class="label label-info label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.allIssues})"/></span></td>
              </tr>
              <tr>
                <td>Blocker Issues</td>
                <td align="right"><span class="label label-danger label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.blockerIssues})"/></span></td>
              </tr>
              <tr>
                <td>Critical Issues</td>
                <td align="right"><span class="label label-danger label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.criticalIssues})"/></span></td>
              </tr>
              <tr>
                <td>Major Issues</td>
                <td align="right"><span class="label label-danger label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.majorIssues})"/></span></td>
              </tr>
              <tr>
                <td>Minor Issues</td>
                <td align="right"><span class="label label-success label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.minorIssues})"/></span></td>
              </tr>
              <tr>
                <td>Info Issues</td>
                <td align="right"><span class="label label-success label-as-badge"><s:property
                    value="getText('number.format.int',{violationStats.infoIssues})"/></span></td>
              </tr>
            </table>


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