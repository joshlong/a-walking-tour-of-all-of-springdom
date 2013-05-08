package org.springsource.examples.spring31.web.config.servlet;

import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.*;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springsource.examples.spring31.web.config.*;

import javax.servlet.*;

/**
 * Simple replacement for <CODE>web.xml</CODE> that is constructed entirely in Java code.
 * This class is picked up at runtime and then given a chance to run when the container starts up,
 * exactly as the <CODE>web.xml</CODE> would be consulted at startup.
 *
 * @author Josh Long
 */
@SuppressWarnings("unused")
public class CrmWebApplicationInitializer implements WebApplicationInitializer {

    private final String patternAll = "/";
    private final String springServletName = "spring";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        registerFilter(servletContext, "springSecurityFilterChain", new DelegatingFilterProxy());
        registerFilter(servletContext, "hiddenHttpMethodFilter", new HiddenHttpMethodFilter());
        registerFilter(servletContext, "multipartFilter", new MultipartFilter());

        servletContext.addListener(new HttpSessionEventPublisher());
        servletContext.addListener(new ContextLoaderListener(buildWebApplicationContext(servletContext, SocialConfiguration.class, SecurityConfiguration.class, WebMvcConfiguration.class)));

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        ServletRegistration.Dynamic spring = servletContext.addServlet(this.springServletName, dispatcherServlet);
        spring.addMapping(patternAll);
        spring.setAsyncSupported(true);

    }

    protected void registerFilter(ServletContext servletContext, String name, Filter filter) {
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(name, filter);
        filterRegistration.addMappingForUrlPatterns(null, true, this.patternAll);
        filterRegistration.addMappingForServletNames(null, true, this.springServletName);
        filterRegistration.setAsyncSupported(true);
    }

    protected WebApplicationContext buildWebApplicationContext(ServletContext servletContext, Class... configClasses) {
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.setServletContext(servletContext);
        ac.register(configClasses);
        ac.refresh();
        return ac;
    }


}
