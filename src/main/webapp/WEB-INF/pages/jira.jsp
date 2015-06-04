<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="jira.title"/></title>
    <meta name="menu" content="Jira"/>
</head>
<body class="home">

<h2><fmt:message key="jira.heading"/></h2>
<p><fmt:message key="jira.message"/></p>

<display:table name="projects" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="users" pagesize="25" class="table table-condensed table-striped table-hover" export="true">
        <display:column property="id" escapeXml="true" sortable="true" titleKey="project.id" style="width: 25%"
                        />
        <display:column property="key" escapeXml="true" sortable="true" titleKey="project.key"
                        style="width: 34%"/>
        <display:column property="name" sortable="true" titleKey="project.name" style="width: 25%" autolink="true"
                        />
        <display:setProperty name="export.excel.filename" value="Project List.xls"/>
        <display:setProperty name="export.csv.filename" value="Project List.csv"/>
        <display:setProperty name="export.pdf.filename" value="Project List.pdf"/>
    </display:table>
</body>