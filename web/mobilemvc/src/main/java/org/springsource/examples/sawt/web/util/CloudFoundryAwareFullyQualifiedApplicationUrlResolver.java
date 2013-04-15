package org.springsource.examples.sawt.web.util;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * 
 * {@link HandlerInterceptorAdapter handler interceptor adapter} that enriches the HTTP request with the application's fully qualified URL.
 * <p/>
 * 
 * This is useful for client side artifacts like <code>javascript</code> and <CODE>css</CODE> resources.
 * <p/>
 * 
 * This class can be used in environments that don't have the {@link org.cloudfoundry.runtime.env.CloudEnvironment cloud environment}
 * object on the CLASSPATH.
 *
 * @author Josh Long
 * 
 */
public class CloudFoundryAwareFullyQualifiedApplicationUrlResolver extends HandlerInterceptorAdapter {

    private Logger log = Logger.getLogger(getClass());

    private String applicationUrlRequestAttributeName = "applicationUrl";

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    private String cloudEnvironmentClassName = "org.cloudfoundry.runtime.env.CloudEnvironment";

    public void setCloudEnvironmentClassName(String cn) {
        this.cloudEnvironmentClassName = cn;
    }

    public void setApplicationUrlRequestAttributeName(String x) {
        this.applicationUrlRequestAttributeName = x;
    }

    private String deriveProtocol(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getProtocol().toLowerCase().contains("https") ? "https" : "http";
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        assert StringUtils.hasText(this.applicationUrlRequestAttributeName) : "the 'applicationUrlRequestAttributeName' property can't be null";

        String uri = null;

        if (ClassUtils.isPresent(this.cloudEnvironmentClassName, this.classLoader)) {

            if (log.isDebugEnabled())
                log.debug("found " + this.cloudEnvironmentClassName + " on the CLASSPATH. ");

            Object cloudEnvironmentObject = ClassUtils.forName(this.cloudEnvironmentClassName, this.classLoader).newInstance();
            boolean cloudFoundry = (Boolean) PropertyUtils.getProperty(cloudEnvironmentObject, "cloudFoundry");

            if (cloudFoundry) {
                @SuppressWarnings("unchecked")
                Collection<String> uris = (Collection<String>) PropertyUtils.getProperty(
                        PropertyUtils.getProperty(cloudEnvironmentObject, "instanceInfo"), "uris");
                StringBuilder builder = new StringBuilder();
                builder.append(deriveProtocol(request)).append("://");
                builder.append(uris.iterator().next());
                builder.append("/");
                builder.append(request.getContextPath());
                uri = builder.toString();
            }
        }
        if (!StringUtils.hasText(uri)) {
            StringBuilder builder = new StringBuilder();
            builder.append(deriveProtocol(request)).append("://");
            builder.append(request.getLocalName());
            builder.append(":");
            builder.append(request.getLocalPort());
            builder.append(request.getContextPath());
            uri = builder.toString();
        }

        assert uri != null && StringUtils.hasText(uri) : "the uri can't be null";


        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        request.setAttribute(this.applicationUrlRequestAttributeName, uri);

        if (log.isDebugEnabled())
            log.debug("setting the current application URI " +
                    "under request attribute '" + this.applicationUrlRequestAttributeName +
                    "', having value " + uri);

        return true;
    }
}
