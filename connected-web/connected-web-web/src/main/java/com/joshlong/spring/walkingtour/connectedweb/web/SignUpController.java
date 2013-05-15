package com.joshlong.spring.walkingtour.connectedweb.web;

import com.joshlong.spring.walkingtour.connectedweb.services.*;
import org.apache.commons.logging.*;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.*;

@Controller
public class SignUpController {


    private static final String testUrl = "/crm/test.html";
    @Inject
    private UserService userService;
    @Inject
    private UsersConnectionRepository usersConnectionRepository;
    @Inject
    private Facebook facebook;
    private Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = testUrl, method = RequestMethod.GET)
    public void test(HttpServletRequest httpServletRequest) throws Throwable {
        System.out.println("is facebook authorized? " +
                this.facebook.isAuthorized());
    }

    @RequestMapping(value = "/crm/signup.html", method = RequestMethod.GET)
    public String signup(HttpServletRequest httpServletRequest) throws Throwable {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            ProviderSignInAttempt signInAttempt = (ProviderSignInAttempt) httpSession.getAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE);
            if (null != signInAttempt) {
                Connection<?> connection = signInAttempt.getConnection();
                UserProfile userProfile = connection.fetchUserProfile();

                User importedUser = importDataFromFacebook(signInAttempt, userProfile);

                ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(importedUser.getUsername());
                connectionRepository.addConnection(connection);

                httpSession.removeAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE);
                return "redirect:/crm/profile.html";
            }
        }
        return "signup";
    }

    protected User importDataFromFacebook(ProviderSignInAttempt providerSignInAttempt,
                                          UserProfile userProfile) throws Throwable {
        User user = null;
        Connection<?> facebookConnection = providerSignInAttempt.getConnection();

        Facebook facebookApiFromConnection = (Facebook) facebookConnection.getApi();

        String username = userProfile.getUsername();
        if ((user = userService.loginByUsername(username)) == null) {
            // then it's a first time thing and we need to sort out a new account and download a profile picture.
            user = userService.createUser(username, "", userProfile.getFirstName(), userProfile.getLastName(), true);

            // now can we import the profile photo?
            UserOperations userOperations = facebookApiFromConnection.userOperations();
            byte[] imageBytes = userOperations.getUserProfileImage(ImageType.LARGE);

            String pathForImage = facebookConnection.getImageUrl();

            // we need to do this in order for the next
            // method call (guarded by spring security pre-authorization checks) to succeed.
            userService.establishSpringSecurityLogin(username);

            if (imageBytes != null && imageBytes.length > 0) {
                userService.writeUserProfilePhoto(user.getId(), pathForImage, imageBytes);
            } else {
                log.debug(String.format("could not import the user's profile photo " +
                        "byte[] buffer for username '%s' and ID # %s", user.getUsername(), user.getId() + ""));
            }

        }


         return user;
    } }
