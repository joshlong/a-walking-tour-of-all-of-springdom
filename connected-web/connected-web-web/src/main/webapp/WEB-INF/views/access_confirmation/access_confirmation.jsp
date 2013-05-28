<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>
<tiles:insertTemplate template="/WEB-INF/layouts/components/box.jsp">
    <tiles:putAttribute name="content">
        <%--
                <div class="panel">
                <fieldset>
                <legend><h2>
                    <spring:message code="profile.update"/>
                </h2></legend>

                <div class="control-group ${cgClass}">--%>

        <style type="text/css">
            .buttons-column {
                width: 100px;
                margin-right: 10px;
                float: left;
            }
        </style>

        <div class="panel">
            <legend><h2>
                <spring:message code="oauth.please-authorize" arguments="${client.clientId}"/>
            </h2></legend>


            <div id="content">


                <authz:authorize ifAllGranted="ROLE_USER">

                    <div class="form-actions"
                         style="background-color: white;  border: 0; margin-bottom :0;margin-top:0;padding-bottom: 0;padding-top: 0;">
                        <p>You hereby authorize <strong>${client.clientId}</strong> to act on your behalf.
                        </p>
                    </div>
                    <form id="confirmationForm" name="confirmationForm"
                          action="${pageContext.request.contextPath}/oauth/authorize" method="post">
                        <fieldset>
                            <div class="form-actions">
                                <div class="buttons-column">
                                    <spring:message var="buttonLabel" code="oauth.authorize"
                                                    arguments="${client.clientId}"/>

                                    <button style="position: inherit;right: 0;" type="submit" name="authorize"
                                            value="${buttonLabel}" class="btn btn-primary">
                                            ${buttonLabel}
                                    </button>

                                    <input name="user_oauth_approval" value="true" type="hidden"/>
                                </div>
                                Authorize <strong>${client.clientId}</strong>  to act on my behalf. Your password will not be shared with
                                the client.
                            </div>
                        </fieldset>

                    </form>
                    <form id="denialForm" name="denialForm" action="${pageContext.request.contextPath}/oauth/authorize"
                          method="post">
                        <fieldset>
                            <div class="form-actions">
                                <div class="buttons-column">
                                    <spring:message var="buttonLabel" code="oauth.deny" arguments="${client.clientId}"/>
                                    <div style="float:left;width: 100px">
                                        <button type="submit" name="deny" value="${buttonLabel}"
                                                class="btn btn-primary">
                                                ${buttonLabel}
                                        </button>
                                    </div>
                                    <input name="user_oauth_approval" value="false" type="hidden"/>
                                </div>

                                You do <strong>not</strong> authorize  <strong>${client.clientId}</strong>  to access your account
                                information and act on your behalf.

                            </div>
                        </fieldset>

                    </form>
                </authz:authorize>
            </div>

        </div>

    </tiles:putAttribute>
</tiles:insertTemplate>


