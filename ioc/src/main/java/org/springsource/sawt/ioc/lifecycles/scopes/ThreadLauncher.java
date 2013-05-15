package org.springsource.sawt.ioc.lifecycles.scopes;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class ThreadLauncher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Log log = LogFactory.getLog(getClass());

    @Inject
    private TaskExecutor scheduler;

    @PostConstruct
    public void launch() throws Throwable {
        for (int i = 0; i < 20; i++)
            scheduler.execute(new ThreadAnnouncerRunnable(this.applicationContext));

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    static class ThreadAnnouncerRunnable implements Runnable {

        private Log log = LogFactory.getLog(getClass());

        private ApplicationContext applicationContext;

        public ThreadAnnouncerRunnable(ApplicationContext ac) {
            this.applicationContext = ac;
        }

        @Override
        public void run() {
            // get first instance
            ThreadAnnouncer announcer;

            if (log.isDebugEnabled())
                log.debug("starting threads for thread " + Thread.currentThread().getName());

            announcer = applicationContext.getBean(ThreadAnnouncer.class);
            announcer.announce();

            announcer = applicationContext.getBean(ThreadAnnouncer.class);
            announcer.announce();

        }
    }

}
