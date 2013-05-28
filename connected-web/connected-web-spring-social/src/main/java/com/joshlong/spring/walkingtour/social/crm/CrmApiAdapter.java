package com.joshlong.spring.walkingtour.social.crm;

import org.apache.commons.logging.*;
import org.springframework.social.connect.*;

public class CrmApiAdapter implements ApiAdapter<CustomerServiceOperations> {
    private Log log = LogFactory.getLog(getClass());

    @Override
    public boolean test(CustomerServiceOperations customerServiceOperations) {
        return (null != customerServiceOperations.currentUser());
    }

    @Override
    public void setConnectionValues(CustomerServiceOperations customerServiceOperations, ConnectionValues values) {
        CrmUserProfile profile = customerServiceOperations.currentUser();
        log.info("profile= " + profile.toString());                          // TODO most of these values are incorrect!
        values.setProviderUserId(Long.toString(profile.getId()));
        values.setDisplayName(profile.getUsername());
        values.setProfileUrl("http://facebook.com/profile.php?id=" + profile.getId()); // todo show the user's profile URL (if any)
        values.setImageUrl("http://graph.facebook.com/" + profile.getId() + "/picture"); // todo show the user's current profile iamge
    }

    @Override
    public UserProfile fetchUserProfile(CustomerServiceOperations customerServiceOperations) {
        CrmUserProfile crmUserProfile = customerServiceOperations.currentUser();
        String name = crmUserProfile.getFirstName() + ' ' + crmUserProfile.getLastName();
        return new UserProfileBuilder()
                .setName(name)
                .setUsername(crmUserProfile.getUsername())
                .setFirstName(crmUserProfile.getFirstName())
                .setLastName(crmUserProfile.getLastName())
                .build();
    }

    @Override
    public void updateStatus(CustomerServiceOperations customerServiceOperations, String message) {
        log.info(String.format("calling updateStatus(CustomerServiceOperations customerServiceOperations, " +
                "String message) with the status '%s', but this method is a no-op!", message));
    }
}
