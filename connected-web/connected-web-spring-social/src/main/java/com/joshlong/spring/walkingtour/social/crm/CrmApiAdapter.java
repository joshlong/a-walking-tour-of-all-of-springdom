package com.joshlong.spring.walkingtour.social.crm;

import org.apache.commons.logging.*;
import org.springframework.social.connect.*;

/**
 *
 * Used by the Spring Social API.
 * </p>
 * Handles the integration between API contract requirements in Spring Social and our API itself.
 * An {@link ApiAdapter} answers questions like: <EM>is the client connected?</EM> and <EM>what is the authenticated user's profile information?</EM>
 *
 * @author Josh Long
 */
public class CrmApiAdapter implements ApiAdapter<CustomerServiceOperations> {
    private Log log = LogFactory.getLog(getClass());

    @Override
    public boolean test(CustomerServiceOperations customerServiceOperations) {
        return (null != customerServiceOperations.currentUser());
    }

    @Override
    public void setConnectionValues(CustomerServiceOperations customerServiceOperations, ConnectionValues values) {
        User profile = customerServiceOperations.currentUser();
        values.setProviderUserId(Long.toString(profile.getId()));
        values.setDisplayName(profile.getUsername());
        if (customerServiceOperations instanceof CustomerServiceTemplate)
            values.setProfileUrl(((CustomerServiceTemplate) customerServiceOperations).urlForPath("/crm/profile.html"));
    }

    @Override
    public  UserProfile fetchUserProfile(CustomerServiceOperations customerServiceOperations) {
        User user = customerServiceOperations.currentUser();
        String name = user.getFirstName() + ' ' + user.getLastName();
        return new UserProfileBuilder()
                .setName(name)
                .setUsername(user.getUsername())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .build();
    }

    @Override
    public void updateStatus(CustomerServiceOperations customerServiceOperations, String message) {
        log.info(String.format("calling updateStatus(CustomerServiceOperations customerServiceOperations, " +
                "String message) with the status '%s', but this method is a no-op!", message));
    }
}
