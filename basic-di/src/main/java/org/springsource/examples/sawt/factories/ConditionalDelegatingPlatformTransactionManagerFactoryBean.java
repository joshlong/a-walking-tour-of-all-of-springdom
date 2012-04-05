package org.springsource.examples.sawt.factories;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * our business requires the ability to run in multiple environments and perform correctly.
 * Part of that is getting the correct {@link PlatformTransactionManager} all the time.
 */
public class ConditionalDelegatingPlatformTransactionManagerFactoryBean implements FactoryBean<PlatformTransactionManager> {

    private DataSource dataSource;

    private String commandLineTriggerVariableName = "test";

    private boolean isTest() {
        String test = System.getProperty(this.commandLineTriggerVariableName);
        return StringUtils.hasText(test) && Boolean.parseBoolean(test);
    }

    public void setCommandLineTriggerVariableName(String commandLineTriggerVariableName) {
        this.commandLineTriggerVariableName = commandLineTriggerVariableName;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PlatformTransactionManager getObject() throws Exception {

        PlatformTransactionManager transactionManager = null;

        if (!isTest() && ClassUtils.isPresent("javax.transaction.Transaction", ClassLoader.getSystemClassLoader())) {
            JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
            jtaTransactionManager.afterPropertiesSet();
            transactionManager = jtaTransactionManager;
        } else {
            Assert.notNull(this.dataSource, "you must provide a DataSource if you intend to create a local DataSourceTransactionManager");
            DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(this.dataSource);
            dataSourceTransactionManager.afterPropertiesSet();
            transactionManager = dataSourceTransactionManager;
        }
        Assert.notNull(transactionManager, "the transactionManager could not be built!");
        return transactionManager;
    }

    @Override
    public Class<?> getObjectType() {
        return PlatformTransactionManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
