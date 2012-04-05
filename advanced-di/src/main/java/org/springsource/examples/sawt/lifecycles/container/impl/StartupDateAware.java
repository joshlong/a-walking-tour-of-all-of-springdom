package org.springsource.examples.sawt.lifecycles.container.impl;

import java.util.Date;

/**
 * marker interface for beans that want to be notified of the date that this context was registered
 */
public interface StartupDateAware {

    void setStartupDate(Date d);
}
