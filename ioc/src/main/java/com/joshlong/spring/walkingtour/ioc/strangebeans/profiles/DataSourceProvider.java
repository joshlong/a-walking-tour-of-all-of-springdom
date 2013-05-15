package com.joshlong.spring.walkingtour.ioc.strangebeans.profiles;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource dataSource();
}
