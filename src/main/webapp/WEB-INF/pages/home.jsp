<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="menu" content="Home"/>
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