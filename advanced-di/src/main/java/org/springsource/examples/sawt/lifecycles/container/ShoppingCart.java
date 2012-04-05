package org.springsource.examples.sawt.lifecycles.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springsource.examples.sawt.lifecycles.container.impl.StartupDateAware;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
@Scope("prototype")
public class ShoppingCart implements StartupDateAware {

    private Log log = LogFactory.getLog(getClass());

    // this will be a new object each time
    @Autowired
    private User user;

    @PostConstruct
    public void begin() throws Throwable {
        configureUserById(Math.random() >= .5 ? 2 : 1);
    }

    public User getUser() {
        return user;
    }

    @Override
    public void setStartupDate(Date d) {
        log.info("STARTUP DATE: the startup date is " + d.toString());
    }

    private void configureUserById(long id) {
        if (id == 1) {
            user.setFirstName("Rod");
            user.setLastName("Johnson");
        } else {
            user.setFirstName("Juergen");
            user.setLastName("Hoeller");
        }
    }
}
