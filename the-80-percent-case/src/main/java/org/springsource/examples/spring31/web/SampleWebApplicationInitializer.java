package org.springsource.examples.spring31.web;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springsource.examples.spring31.services.CustomerService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

public class SampleWebApplicationInitializer implements WebApplicationInitializer {

    private AnnotationConfigWebApplicationContext applicationContextForServletContext(ServletContext sc, String... pkgs) {
        final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(sc);
        applicationContext.scan(pkgs);
        return applicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext servicesContext = applicationContextForServletContext(servletContext, CustomerService.class.getPackage().getName());
        servletContext.addListener(new ContextLoaderListener(servicesContext));
        servletContext.addFilter("hiddenMethodHttpFilter", new HiddenHttpMethodFilter());

        AnnotationConfigWebApplicationContext webContext = applicationContextForServletContext(servletContext, CustomerController.class.getPackage().getName());
        ServletRegistration.Dynamic servlet = servletContext.addServlet("spring", new DispatcherServlet(webContext));
        servlet.setLoadOnStartup(1);
        Set<String> conflicts = servlet.addMapping("/");

        if (!conflicts.isEmpty())
            throw new IllegalStateException("'appServlet' could not be mapped to '/' due "
                    + "to an existing mapping. This is a known issue under Tomcat versions "
                    + "<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");

    }
}
