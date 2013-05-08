package org.springsource.examples.spring31.services.config;

import java.util.Map;

/**
 * This interface extracts the things that change from one environment to another
 * into a separate hierarchy so that implementations may be
 * <em>activated</em> based on which profile is active.
 *
 * @author Josh Long
 */
public interface DataSourceConfiguration {

    Map<String, String> contributeJpaEntityManagerProperties() throws Exception;
}