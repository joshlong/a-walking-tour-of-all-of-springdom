<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<div ng-controller="SignInController">
    <tiles:insertTemplate template="/WEB-INF/layouts/components/box.jsp">
        <tiles:putAttribute name="content">

            <form:form class="form-horizontal" id="form" name="form" method="POST" action="j_spring_security_check">

                <div class="panel">
                    <fieldset>

                        <legend><h2>
                            <spring:message code="login.signin"/>
                        </h2></legend>


                        <div class="control-group error">
                            <UL>
                                <c:if test="${param.error == 'true'}">
                                    <li><spring:message code="login.invalid"/></li>
                                </c:if>
                            </UL>
                        </div>

                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="j_username">
                                <spring:message code="login.username"/>:</label>

                            <div class="controls">
                                <input class="input-xlarge" id="j_username" name="j_username" type="text"
                                       ng-model="user.username" required="required"/>
                                <span ng-show="${error}" class="help-inline"><spring:message
                                        code="login.email.prompt"/></span>
                            </div>
                        </div>
                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="j_password"><spring:message
                                    code="login.password"/>:</label>

                            <div class="controls">
                                <input class="input-xlarge" id="j_password" name="j_password" type="password"
                                       ng-model="user.password" required="required"/>
                                <span ng-show="${error}" class="help-inline">  <spring:message
                                        code="login.password.prompt"/> </span>
                            </div>
                        </div>

                        <div class="form-actions">

                            <button type="submit" ng-disabled="form.$invalid" class="btn btn-primary" name="action"
                                    value="signin" ng-model-instant>
                                <spring:message code="login.signin"/>
                            </button>



                                <spring:message code="login.buttons.dontHaveAccount"/>

                                <span>
                                    <a href="${pageContext.request.contextPath}/crm/signup.html"><spring:message
                                            code="login.buttons.register"/></a>
                                    |
                                    <a ng-click="signinWithFacebook()" href="javascript:void(0);"><spring:message
                                            code="login.buttons.withFacebook"/></a>
                                </span>

                    </fieldset>
                </div>
            </form:form>
            <c:url var="signinWithProvider" value="/signin/facebook"/>
            <form method="POST" id="signinWithFacebook" action="${signinWithProvider}">
            </form>


        </tiles:putAttribute>
    </tiles:insertTemplate>
</div>