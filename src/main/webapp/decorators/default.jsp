<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html lang="en">
<head>
    <%--<meta http-equiv="Cache-Control" content="no-store"/>--%>
    <%--<meta http-equiv="Pragma" content="no-cache"/>--%>
    <%--<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>--%>
    <%--<meta http-equiv="X-UA-Compatible" content="IE=edge"/>--%>
    <%--<meta name="viewport" content="width=device-width, initial-scale=1.0">--%>
    <%--<link rel="icon" href="<c:url value="/images/favicon.ico"/>"/>--%>
    <%--<title><decorator:title/> | <fmt:message key="webapp.name"/></title>--%>
    <%--&lt;%&ndash;<t:assets type="css"/>&ndash;%&gt;--%>
    <%--<decorator:head/>--%>
</head>
<body
    <%--<decorator:getProperty property="body.id" writeEntireProperty="true"/>--%>
        <%--<decorator:getProperty property="body.class" writeEntireProperty="true"/>--%>
    >
    <%--<c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>--%>

    <%--<div class="navbar navbar-default navbar-fixed-top" role="navigation">--%>
        <%--<div class="navbar-header">--%>
            <%--<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">--%>
                <%--<span class="icon-bar"></span>--%>
                <%--<span class="icon-bar"></span>--%>
                <%--<span class="icon-bar"></span>--%>
            <%--</button>--%>
            <%--<div>--%>
                <%--<a href="<c:url value='/'/>">--%>
                <%--&lt;%&ndash;<fmt:message key="webapp.name"/>&ndash;%&gt;--%>
                <%--<img src="images/jarvis50.png" alt="Jarvis Logo"/>--%>
                <%--</a>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<%@ include file="/common/menu.jsp" %>--%>
        <%--<c:if test="${pageContext.request.locale.language != 'en'}">--%>
            <%--<div id="switchLocale"><a href="<c:url value='/?locale=en'/>">--%>
                <%--<fmt:message key="webapp.name"/> in English</a>--%>
            <%--</div>--%>
        <%--</c:if>--%>
    <%--</div>--%>

    <div >
        <%--<%@ include file="/common/messages.jsp" %>--%>
        <%--<div class="row">--%>
            <decorator:body/>

            <%--<c:if test="${currentMenu == 'AdminMenu'}">--%>
                <%--<div class="col-sm-2">--%>
                <%--<menu:useMenuDisplayer name="Velocity" config="navlistMenu.vm" permissions="rolesAdapter">--%>
                    <%--<menu:displayMenu name="AdminMenu"/>--%>
                <%--</menu:useMenuDisplayer>--%>
                <%--</div>--%>
            <%--</c:if>--%>
        <%--</div>--%>
    </div>

    <%--<div id="footer" class="container navbar-fixed-bottom">--%>
        <%--<span class="col-sm-6 text-left"><fmt:message key="webapp.version"/>--%>
            <%--<c:if test="${pageContext.request.remoteUser != null}">--%>
            <%--| <fmt:message key="user.displayName"/> ${pageContext.request.remoteUser}--%>
            <%--</c:if>--%>
        <%--</span>--%>
        <%--<span class="col-sm-6 text-right">--%>
            <%--&copy; <fmt:message key="copyright.year"/> <a href="<fmt:message key="company.url"/>"><fmt:message key="company.name"/></a>--%>
        <%--</span>--%>
    <%--</div>--%>
<%--<t:assets type="js"/>    --%>
<%--<%= (request.getAttribute("scripts") != null) ?  request.getAttribute("scripts") : "" %>--%>
</body>
</html>
