<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="stash.title"/></title>
  <meta name="menu" content="Stash"/>
</head>
<body class="body-bg">

<h2><fmt:message key="stash.heading"/></h2>

<p><fmt:message key="stash.message"/></p>


<div class="panel panel-default">
  <display:table name="page.values" cellspacing="0" cellpadding="0" requestURI=""
                 defaultsort="1" id="project" pagesize="25" class="table table-condensed table-striped table-hover"
                 export="true">
    <display:column property="id" escapeXml="true" sortable="true" titleKey="project.id" style="width: 5%"/>
    <display:column escapeXml="false" sortable="true" titleKey="project.name" style="width: 25%" media="html">
      <a href="<c:out value="${baseUrl}${project.link.url}"/>">
        <c:out value="${project.name}"/>
      </a>
    </display:column>
    <display:column property="key" escapeXml="true" sortable="true" titleKey="project.key" style="width: 5%"/>
    <display:column property="description" escapeXml="true" sortable="true" titleKey="project.description"
                    style="width: 25%"/>
    <display:column property="type" escapeXml="true" sortable="true" titleKey="project.type" style="width: 5%"/>
    <display:setProperty name="export.excel.filename" value="Stash Project List.xls"/>
    <display:setProperty name="export.csv.filename" value="Stash Project List.csv"/>
    <display:setProperty name="export.pdf.filename" value="Stash Project List.pdf"/>
  </display:table>
</div>
</body>