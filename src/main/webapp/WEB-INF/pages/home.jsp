<%@ include file="/common/taglibs.jsp" %>

<head>
  <title><fmt:message key="home.title"/></title>
  <meta name="menu" content="Home"/>
  <!-- Loading Flat UI -->
  <link href="../../styles/flat-ui.css" rel="stylesheet">
  <link href="../../styles/css/style1.css" rel="stylesheet" type="text/css"/>
</head>
<body class="body-bg">

<h2><fmt:message key="home.heading"/></h2>

<p><fmt:message key="home.message"/></p>

<ul class="glassList">
  <li>
    <a href="<c:url value='/jira'/>"><fmt:message key="menu.jira"/></a>
  </li>

  <li>
    <a href="<c:url value='/sessions'/>"><fmt:message key="menu.session"/></a>
  </li>
  <!--
    <li>
        <a href="<c:url value='/selectFile'/>"><fmt:message key="menu.selectFile"/></a>
    </li>
    -->
</ul>
</body>