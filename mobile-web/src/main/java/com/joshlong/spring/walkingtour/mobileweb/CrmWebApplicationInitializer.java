package com.joshlong.spring.walkingtour.mobileweb;

import org.apache.commons.logging.*;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * @author Josh Long
 */
public class CrmWebApplicationInitializer /* implements WebApplicationInitializer*/ {
    private Log log = LogFactory.getLog(getClass());
    private String patternAll = "/";
    private String springServletName = "spring";

     public void onStartup(ServletContext servletContext) throws ServletException {

        registerFilter(servletContext, "hiddenHttpMethodFilter", new HiddenHttpMethodFilter());
        registerFilter(servletContext, "multipartFilter", new MultipartFilter());

        servletContext.addListener(new ContextLoaderListener(buildWebApplicationContext(servletContext, MvcConfiguration.class)));

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        ServletRegistration.Dynamic spring = servletContext.addServlet(this.springServletName, dispatcherServlet);
        spring.addMapping(patternAll);
        spring.setAsyncSupported(true);

    }

    protected void registerFilter(ServletContext servletContext, String name, Filter filter) {
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(name, filter);
        filterRegistration.setAsyncSupported(true);
        filterRegistration.addMappingForUrlPatterns(null, true, this.patternAll);
        filterRegistration.addMappingForServletNames(null, true, this.springServletName);
    }

    protected WebApplicationContext buildWebApplicationContext(ServletContext servletContext, Class... configClasses) {
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.setServletContext(servletContext);
        ac.register(configClasses);
        ac.refresh();
        return ac;
    }


}
