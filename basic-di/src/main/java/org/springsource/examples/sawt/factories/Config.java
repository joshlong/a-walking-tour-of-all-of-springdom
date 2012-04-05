package org.springsource.examples.sawt.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackageClasses = Config.class, excludeFilters = {@ComponentScan.Filter(Configuration.class)})
public class Config {

    @Bean
    public AutoToStringFactoryBean ptm() throws Exception {
        PlatformTransactionManager platformTransactionManager = conditionalPlatformTransactionManager().getObject();
        AutoToStringFactoryBean autoToStringFactoryBean = new AutoToStringFactoryBean();
        autoToStringFactoryBean.setTarget(platformTransactionManager);
        autoToStringFactoryBean.setProxyTargetClass(true);
        return autoToStringFactoryBean;
    }

    @Bean
    public ConditionalDelegatingPlatformTransactionManagerFactoryBean conditionalPlatformTransactionManager() {
        ConditionalDelegatingPlatformTransactionManagerFactoryBean ds = new ConditionalDelegatingPlatformTransactionManagerFactoryBean();
        ds.setDataSource(this.embeddedDatabase());
        return ds;
    }

    @Bean
    public EmbeddedDatabase embeddedDatabase() {
        EmbeddedDatabaseBuilder edb = new EmbeddedDatabaseBuilder();
        edb.setType(EmbeddedDatabaseType.H2);
        return edb.build();
    }
}
