package org.springsource.examples.sawt.web.mvc;


public class MvcWebApplicationInitializer /* implements WebApplicationInitializer */ {

/** 
 *     private AnnotationConfigWebApplicationContext applicationContextForServletContext(
            ServletContext sc, String... pkgs) {
        final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(sc);
        applicationContext.scan(pkgs);
        return applicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {

        AnnotationConfigWebApplicationContext webContext = applicationContextForServletContext(servletContext, new String[]{JpaCustomerService.class.getPackage().getName(), CustomerController.class.getPackage().getName()});

        ServletRegistration.Dynamic servlet = servletContext.addServlet("spring", new DispatcherServlet(webContext));
        servlet.setLoadOnStartup(1);
        Set<String> conflicts = servlet.addMapping("/");

        if (!conflicts.isEmpty())
            throw new IllegalStateException(
                    "'appServlet' could not be mapped to '/' due "
                            + "to an existing mapping. This is a known issue under Tomcat versions "
                            + "<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");

    }

 */
}
