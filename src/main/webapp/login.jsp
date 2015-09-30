<%@ include file="/common/taglibs.jsp" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title></title>
  <title><fmt:message key="login.title"/></title>
  <meta name="menu" content="Login"/>

  <!--Stylesheets-->
  <link href="styles/timeline/css/bootstrap.min.css" rel="stylesheet">
  <link href="styles/timeline/css/login.css" rel="stylesheet">

</head>
<body>
<div class="container-fluid">
  <div class="container vcenter">
    <div class="row text-center">
      <div class="col-lg-offset-4 col-lg-4 col-md-offset-4 col-md-4 col-sm-12 col-xs-12 fixed-size">
        <div class="logo"></div>
        <div class="welcome">New way of project management in Open Source</div>
        <div class="form text-left">
          <form method="post" id="loginForm" action="<c:url value='/j_security_check'/>"
                onsubmit="saveUsername(this);return validateForm(this)" autocomplete="off">

            <c:if test="${param.error != null}">
              <div class="alert alert-danger alert-dismissable">
                <fmt:message key="errors.password.mismatch"/>
              </div>
            </c:if>

            <div class="form-group">
              <input type="text" name="j_username" id="j_username" class="form-control"
                     placeholder="<fmt:message key="label.name"/>" required tabindex="1" placeholder="Username">
            </div>
            <div class="form-group">
              <input type="password" class="form-control" name="j_password" id="j_password" tabindex="2"
                     placeholder="<fmt:message key="label.password"/>" required>
            </div>


            <c:if test="${appConfig['rememberMeEnabled']}">
              <div class="checkbox">
                <label>
                  <input type="checkbox" name="_spring_security_remember_me" id="rememberMe" tabindex="3"/> Keep me logged in.
                </label>
              </div>
            </c:if>


            <div class="login text-center"><button type="submit" class="btn btn-default" name="login">LOGIN</button></div>

            <div class="signup text-center">
              <p>
                <fmt:message key="login.signup">
                  <fmt:param><c:url value="/signup"/></fmt:param>
                </fmt:message>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<c:set var="scripts" scope="request">
  <%@ include file="/scripts/login.js" %>
</c:set>

</body>
</html>