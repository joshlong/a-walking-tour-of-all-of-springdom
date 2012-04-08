package org.springsource.examples.sawt.web.gwt.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(org.springsource.examples.sawt.services.jdbc.Config.class)
public class WebConfig {
}
