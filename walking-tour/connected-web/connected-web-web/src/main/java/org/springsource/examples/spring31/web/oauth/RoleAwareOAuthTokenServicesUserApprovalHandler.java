package org.springsource.examples.spring31.web.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.TokenServicesUserApprovalHandler;
import org.springsource.examples.spring31.services.UserService;

/**
 * Simple {@link org.springframework.security.oauth2.provider.approval.UserApprovalHandler} implementation that's aware
 * of OAuth's {@link org.springframework.security.core.token.TokenService token services}
 *
 * @author Josh Long
 */
public class RoleAwareOAuthTokenServicesUserApprovalHandler extends TokenServicesUserApprovalHandler {


    private boolean useTokenServices = true;

    /**
     * @param useTokenServices the useTokenServices to set
     */
    public void setUseTokenServices(boolean useTokenServices) {
        this.useTokenServices = useTokenServices;
    }



    /**
     * Allows automatic approval for a white list of clients in the implicit grant case.
     *
     * @param authorizationRequest The authorization request.
     * @param userAuthentication   the current user authentication
     * @return Whether the specified request has been approved by the current user.
     */
    @Override
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {

        assert userAuthentication != null : "the userAuthentication can't be null";
        assert authorizationRequest != null : "the authorizationRequest can't be null";

        UserService.CrmUserDetails crmUserDetails = (UserService.CrmUserDetails) userAuthentication.getPrincipal();

        if (useTokenServices && super.isApproved(authorizationRequest, userAuthentication)) {
            return true;
        }

        if (!userAuthentication.isAuthenticated()) {
            return false;
        }

        String flag = authorizationRequest.getApprovalParameters().get(AuthorizationRequest.USER_OAUTH_APPROVAL);
        boolean approved = flag != null && flag.trim().toLowerCase().equals("true");

        boolean tokenApproved =
                (authorizationRequest.getResponseTypes().contains("token") && null != crmUserDetails &&
                        crmUserDetails.getUser() != null &&
                        crmUserDetails.getUser().getId() > 0);
        return approved || tokenApproved;

    }

}