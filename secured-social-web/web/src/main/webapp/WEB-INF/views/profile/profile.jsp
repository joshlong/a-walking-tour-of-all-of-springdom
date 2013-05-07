<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>
<div ng-controller="ProfileController">
    <tiles:insertTemplate template="/WEB-INF/layouts/components/box.jsp">
        <tiles:putAttribute name="content">
            <form class="form-horizontal" id="form" name="form" ng-submit="saveProfileData()">

                <div class="panel">
                    <fieldset>
                        <legend><h2>
                            <spring:message code="profile.update"/>
                        </h2></legend>

                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="username">
                                <spring:message code="profile.email"/>
                            </label>

                            <div class="controls"><input class="input-xlarge"
                                                         id="username"
                                                         name="username"
                                                         type="text"
                                                         ui-validate="{ validUsername : isUsernameValid }"
                                                         required="required"
                                                         ng-model="user.username"/>
                                <span ng-show="${error}" class="help-block">  <spring:message code="profile.email.prompt"/>   </span>
                                <span ng-show="form.username.$error.validUsername" class="help-block"> <spring:message code="profile.email.error.invalid"/> </span>
                                <span ng-show="usernameTaken" class="help-block"> <spring:message code="profile.email.error.taken"/> </span>
                            </div>
                        </div>
                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="password"> <spring:message code="profile.password"/>:</label>

                            <div class="controls">
                                <input class="input-xlarge" id="j_password" id="password" type="password" ng-model="user.password" required="required"/>
                                <span ng-show="${error}" class="help-inline"> <spring:message code="profile.password.error.invalid"/> </span>
                            </div>
                        </div>
                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="password"> <spring:message
                                    code="profile.password-confirm"/>:</label>

                            <div class="controls">
                                <input class="input-xlarge" ui-validate="{ confirmPassword : confirmPasswordMatches }"
                                       type="password"
                                       name="passwordConfirmation"
                                       ng-model="user.passwordConfirmation" required="required"/>
                                 <span ng-show="form.passwordConfirmation.$error.confirmPassword" class="help-block">
                                    <spring:message code="profile.passwords.dont-match"/>
                                 </span>
                                <span ng-show="${error}" class="help-inline">  <spring:message code="profile.passwords.prompt"/>   </span>
                            </div>
                        </div>

                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="firstName">
                                <spring:message code="profile.first-name"/>:</label>

                            <div class="controls"><input class="input-xlarge" id="firstName" type="text"  ng-model="user.firstName" required="required"/>
                                <span ng-show="${error}" class="help-inline">  <spring:message code="profile.first-name.prompt"/> </span>
                            </div>
                        </div>

                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="lastName"> <spring:message code="profile.last-name"/>:</label>

                            <div class="controls"><input class="input-xlarge" id="lastName" type="text"
                                                         ng-model="user.lastName" required="required"/>
                                <span ng-show="${error}" class="help-inline">  <spring:message
                                        code="profile.last-name.prompt"/>  </span>
                            </div>
                        </div>
                        <div class="control-group ${cgClass}">
                            <label class="control-label" for="j_password"><spring:message code="profile.photo"/>:</label>

                            <div class="controls">

                                <div id="profilePhoto">
                                    <spring:message code="profile.drag-photo-here"/>
                                </div>


                            </div>
                        </div>

                        <div class="form-actions">
                            <button type="submit" ng-disabled="form.$invalid" class="btn btn-primary" ng-model-instant>
                                <spring:message code="profile.buttons.save"/>
                            </button>


                        </div>

                    </fieldset>
                </div>
            </form>

        </tiles:putAttribute>

    </tiles:insertTemplate>


</div>



