<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="sessionList.title"/></title>
  <meta name="menu" content="SessionMenu"/>
  <!-- Loading Flat UI -->
  <link href="../../styles/flat-ui.css" rel="stylesheet">
  <link href="../../styles/css/style1.css" rel="stylesheet" type="text/css"/>
</head>
<body class="body-bg">
<div class="col-sm-10">
  <h2><fmt:message key="sessionList.heading"/></h2>

  <div id="actions" class="btn-group">
    <a class="btn btn-default" href="<c:url value="/home"/>">
      <i class="icon-ok"></i> <fmt:message key="button.done"/>
    </a>
  </div>

  <div class="panel panel-default">
    <display:table name="sessions" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="sessions" pagesize="25" class="table table-condensed table-striped table-hover"
                   export="true">
      <display:column property="id" escapeXml="true" sortable="true" titleKey="session.id"/>
      <display:column property="issueKey" escapeXml="true" sortable="true" titleKey="session.issue.key"/>
      <display:column property="username" escapeXml="true" sortable="true" titleKey="session.username"
                      style="width: 25%"/>
      <display:column property="status" escapeXml="true" sortable="true" titleKey="session.status"
                      style="width: 34%"/>
      <display:column property="created" sortable="true" titleKey="session.created" style="width: 25%"/>

      <display:setProperty name="paging.banner.item_name"><fmt:message key="sessionList.session"/></display:setProperty>
      <display:setProperty name="paging.banner.items_name"><fmt:message
          key="sessionList.sessions"/></display:setProperty>

      <display:setProperty name="export.excel.filename" value="Session List.xls"/>
      <display:setProperty name="export.csv.filename" value="Session List.csv"/>
      <display:setProperty name="export.pdf.filename" value="Session List.pdf"/>
    </display:table>
  </div>
</div>
</body>
