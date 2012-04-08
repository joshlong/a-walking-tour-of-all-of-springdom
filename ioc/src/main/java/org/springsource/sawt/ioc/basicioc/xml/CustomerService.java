package org.springsource.sawt.ioc.basicioc.xml;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.sql.DataSource;

/**
 * Example class that requires a {@link javax.sql.DataSource}
 */
public class CustomerService {

    private DataSource dataSource;

    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        System.out.println( "the data source has been set " + ToStringBuilder.reflectionToString(this.dataSource));

    }


    // ....
    // ....
    //

}
