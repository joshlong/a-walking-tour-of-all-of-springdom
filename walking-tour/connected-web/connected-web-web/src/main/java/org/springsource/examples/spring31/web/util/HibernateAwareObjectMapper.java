package org.springsource.examples.spring31.web.util;


import com.fasterxml.jackson.module.hibernate.HibernateModule;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Provides an implementation of Jackson's {@link ObjectMapper object mapper contract}, which
 * makes it aware of Hibernate's lazy collections, which it prunes before rendering.
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
    public HibernateAwareObjectMapper() {
        HibernateModule hm = new HibernateModule();
        registerModule(hm);
        configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    public void setPrettyPrint(boolean prettyPrint) {
        configure(SerializationConfig.Feature.INDENT_OUTPUT, prettyPrint);
    }
}
