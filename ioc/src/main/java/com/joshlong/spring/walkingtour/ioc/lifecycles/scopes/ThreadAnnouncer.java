package com.joshlong.spring.walkingtour.ioc.lifecycles.scopes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean is scoped per thread, so successive accesses of this bean in a single
 * thread will always print out the same 'name' but in different threads the
 * access will yeild new {@link ThreadAnnouncer announcers} with unique names.
 */
public class ThreadAnnouncer {

	private Log log = LogFactory.getLog(getClass());

	private String name;

	public ThreadAnnouncer() {
		// cache the current thread for each new construction
		this.name = Thread.currentThread().getName();
	}

	public void announce() {
		System.out.println("running on thread " + this.name);
	}
}
