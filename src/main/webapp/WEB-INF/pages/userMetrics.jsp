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
</head>


<body class="body-bg">

<h2><fmt:message key="userMetrics.heading"/></h2>


<div class="container-fluid">

  <div class="row">
    <div class="col-sm-4">
      <div class="panel panel-default">
        <div align="center" style="padding: 15px 15px">
          <s:form action="usermetrics" enctype="application/x-www-form-urlencoded" method="post" validate="true"
                  id="userMetricsForm"
                  cssClass="">

            <s:select label="Project"
                      headerKey="-1" headerValue="--- Select ---"
                      list="projects"
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

</div>


<div class="container">
  <div id="content">
    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
      <s:iterator value="members">
        <li><a href="#red" data-toggle="tab"><s:property value="name"/></a></li>
      </s:iterator>
    </ul>

    <br>

    <c:if test="${members != null}">
      <div id="my-tab-content" class="tab-content">
        <div class="row">
          <div class="col-md-4">

            <div class="panel panel-default">
              <div class="panel-heading">Developer Metrics</div>
              <div class="panel-body">

                <table class="table table-striped table-bordered table-hover" cellpadding="2">
                  <tr>
                    <td>Line of code</td>
                    <td class="col-md-2" align="right"><span class="label label-primary label-as-badge">55</span></td>
                  </tr>
                  <tr>
                    <td>Velocity</td>
                    <td align="right"><span class="label label-primary label-as-badge">90%</span></td>
                  </tr>
                  <tr>
                    <td>Work ratio</td>
                    <td align="right"><span class="label label-primary label-as-badge">85%</span></td>
                  </tr>
                  <tr>
                    <td>Code coverage</td>
                    <td align="right"><span class="label label-primary label-as-badge">95%</span></td>
                  </tr>
                </table>

              </div>
            </div>
          </div>
        </div>
      </div>
    </c:if>

  </div>
</div>

</body>
</html>
