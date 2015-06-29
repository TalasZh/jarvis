<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="confluence.title"/></title>
  <meta name="menu" content="Confluence"/>
</head>
<body class="home">

<h2><fmt:message key="confluence.heading"/></h2>

<p><fmt:message key="confluence.message"/></p>

<display:table name="spaces" cellspacing="0" cellpadding="0" requestURI="" export="true" id="space"
               defaultsort="1" pagesize="25" class="table table-condensed table-striped table-hover">
  <display:column property="id" escapeXml="true" sortable="true" titleKey="confluence.space.id" style="width: 5%"/>
  <display:column escapeXml="name" sortable="true" titleKey="confluence.space.name" style="width: 25%" media="html">
    <a href="<c:out value="${baseUrl}/display/${space.key}"/>">
      <c:out value="${space.name}"/>
    </a>
  </display:column>
  <display:column property="key" escapeXml="true" sortable="true" titleKey="confluence.space.key" style="width: 5%"/>
  <display:column property="description.plain.value" escapeXml="true" sortable="true" titleKey="confluence.space.description" media="html"
                  style="width: 25%"/>
  <display:column property="type" escapeXml="true" sortable="true" titleKey="confluence.space.type" style="width: 5%" />
  <display:setProperty name="export.excel.filename" value="Confluence Page List.xls"/>
  <display:setProperty name="export.csv.filename" value="Confluence Page List.csv"/>
  <display:setProperty name="export.pdf.filename" value="Confluence Page List.pdf"/>
</display:table>
</body>