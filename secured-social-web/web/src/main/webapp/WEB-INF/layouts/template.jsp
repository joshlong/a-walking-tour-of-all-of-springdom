<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>
<!doctype html>
<html ng-app="crm">
<head>
    <%-- Hack for IE. bleargh. --%>
    <%--    <!--[if IE]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->--%>

    <script type="text/javascript">

        var crmSession = {

            getUsername: function () {
                var un = "${username}";
                var pi;
                if (un == '')
                    return null;
                return un;
            },
            isLoggedIn: function () {
                return !(this.getUserId() == null );
            },
            getUserId: function () {
                var uid = "${userId}";
                var pi;
                if (uid == '' || (pi = parseInt(uid)) == 0) {
                    return null;
                }
                return pi;
            }
        };

    </script>

    <%--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>--%>

    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/jquery-ui.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/jquery-filedrop.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/jquery-masked.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/angular.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/angular-ui.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/angular-resource.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/jso.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/web/views/controllers.js"></script>
    <link href="${pageContext.request.contextPath}/web/assets/css/jquery-ui.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/web/assets/bootstrap/bootstrap.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/web/views/controllers.css" rel="stylesheet"/>
    <!--[if lte IE 8]>
    <script src="build/angular-ui-ieshiv.js"></script><![endif]-->
</head>
<body>


<div id="navigation">


    <div ng-controller="NavigationController">

        <security:authorize access="isAnonymous()">
            <A href="${pageContext.request.contextPath}/crm/signin.html">Sign In</a>
        </security:authorize>
        <security:authorize access="isAuthenticated()">

            <c:url value="/j_spring_security_logout" var="logoutUrl"/>

            Welcome <strong><security:authentication property="principal.username"/></strong> |
            <span>
                <a href="${pageContext.request.contextPath}/crm/profile.html">My Profile</a> |
                <a href="${pageContext.request.contextPath}/crm/customers.html">My Customers</a> |
                <a ng-click="startLogoutFlow( '${logoutUrl}' )" href="${logoutUrl}">Sign Out</a>
            </span>
        </security:authorize>

    </div>
</div>
<div id="content">
    <tiles:insertAttribute name="content"/>
</div>


<%--
 browsers tend to render css at the point they're encontered,
 which triggers a repaint delay and an awkward pause
 while the file is downloaded for the first time. best to put this
 at the end so that everything appears to instantly snap into place
 rather than render slowly once they've arrived already
--%>

<div id="copyright">
<%--
    <spring:message code="brought-to-you-by"/> <A href="http://www.springsource.org">SpringSource</a>.
--%>
</div>
</body>
</html>