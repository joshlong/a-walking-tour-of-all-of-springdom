package org.springsource.sawt.ioc.strangebeans.profiles;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource dataSource();
}
