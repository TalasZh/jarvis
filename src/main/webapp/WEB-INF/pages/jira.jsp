<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="jira.title"/></title>
  <meta name="menu" content="Jira"/>
</head>
<body class="home">

<h2><fmt:message key="jira.heading"/></h2>

<p><fmt:message key="jira.message"/></p>


<div class="container-fluid">

  <div class="row">
    <div class="col-sm-7">
      <s:form action="jira" enctype="application/x-www-form-urlencoded" method="post" validate="true" id="jiraForm"
              cssClass="well">

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


<display:table name="issues" cellspacing="0" cellpadding="0" requestURI=""
               defaultsort="1" id="issue" pagesize="25" class="table table-condensed table-striped table-hover"
               export="true">
  <display:column property="id" escapeXml="true" sortable="true" titleKey="issue.id" style="width: 10%"
      />

  <display:column escapeXml="false" sortable="true" titleKey="issue.key" style="width: 10%" media="html">
    <a href="<c:out value="${jiraUrl}/browse/${issue.key}"/>">
      <c:out value="${issue.key}"/>
    </a>
  </display:column>

  <display:column property="summary" sortable="true" titleKey="issue.summary" style="width: 55%" autolink="true"
      />
  <display:column property="status" sortable="true" titleKey="issue.status" style="width: 5%" autolink="true"
      />
  <display:column property="assignee" sortable="true" titleKey="issue.assignee" style="width: 10%"
                  autolink="true"
      />
  <display:column property="reporter" sortable="true" titleKey="issue.reporter" style="width: 10%" autolink="true"
      />
  <display:setProperty name="export.excel.filename" value="Issue List.xls"/>
  <display:setProperty name="export.csv.filename" value="Issue List.csv"/>
  <display:setProperty name="export.pdf.filename" value="Issue List.pdf"/>
</display:table>

</body>